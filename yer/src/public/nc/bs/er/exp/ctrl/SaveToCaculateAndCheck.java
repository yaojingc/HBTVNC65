package nc.bs.er.exp.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.bs.er.exp.cmd.ServiceFeeCalcVO;
import nc.bs.er.exp.util.ExpUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.im.exception.BusinessException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;
import nc.ui.erm.billpub.action.HRATCommonUtils;
import nc.ui.erm.billpub.action.IdCardUtil;
import nc.ui.erm.billpub.action.ServiceFeeTaxUtils;
import nc.vo.pub.lang.UFDouble;

/**
 * 
 * 
 * defitem44 表体实发金额
 * 
 * 
 * 此类先对界面上导入的数据做校验，保证计算之前的数据合法性 其次校验几个重要字段 身份信息等
 * 
 * @author yao
 * 
 */
public class SaveToCaculateAndCheck {

	private LfwView mainWidget;

	private Dataset headData;

	private Dataset BodyData;

	// 设置一个静态全局变量，在调用校验方法的时候只初始化一次表体行
	private static Row[] rows;

	private static Dataset dataset;
	
	private IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);

	/**
	 * 通过构造方法初始化几个共用对象
	 * 
	 * @param mainWidget
	 * @param headData
	 * @param BodyData
	 */
	public SaveToCaculateAndCheck(LfwView mainWidget, Dataset headData,
			Dataset bodyData) {
		this.mainWidget = mainWidget;
		this.headData = headData;
		this.BodyData = bodyData;
	}
	
	
	
	/**
	 * 先校验界面上的数据
	 * @param mainWidget
	 * @param headData
	 * @param BodyData
	 * @return
	 */
	public String checkData() {
		// 错误信息
		StringBuilder errMsg = new StringBuilder();
		// 得到整个页面的数据集
		Dataset[] datasets = mainWidget.getViewModels().getDatasets();
		
		// 定义两个集合分别存储 劳务费和稿费
		Map<String, String> laowufeiMap = new HashMap<>();
		Map<String, String> gaofeiMap = new HashMap<>();
		// 报错信息
		StringBuilder errMsgTmp01 = new StringBuilder();// 劳务费
		StringBuilder errMsgTmp02 = new StringBuilder();// 稿费
		String laowufei="劳务费";
		String gaofei="稿费";

		for (Dataset dataset : datasets) {
			// 属于页签数据
			if (dataset instanceof MdDataset) {
				String dataId = dataset.getId();
				if ("busitem".equalsIgnoreCase(dataId)) {
					// 得到所有的表体行数据
					rows = dataset.getAllRow();
					this.dataset = dataset;
					if (HRATCommonUtils.isNotEmpty(rows)) {
						// 循环表体行,计算本次预计扣税以及增补扣税，累加 税金合计、实发合计
						for (int i = 0; i < rows.length; i++) {
							String rowStr = "第" + (i + 1) + "行:";
							errMsg.append(rowStr);
							Boolean flag = true;
							// 实发金额 defitem44
							UFDouble defitem44 = rows[i].getValue(dataset.nameToIndex("defitem44")) == null ? new UFDouble(0) : new UFDouble(rows[i].getValue(dataset.nameToIndex("defitem44")).toString());
							// 应发金额 amount
							UFDouble amount = rows[i].getValue(dataset.nameToIndex("amount")) == null ? new UFDouble(0) : new UFDouble(rows[i].getValue(dataset.nameToIndex("amount")).toString());
							// 获取劳务费类型主键
							String serviceType = (String) rows[i].getValue(dataset.nameToIndex("defitem50"));
							// 在对应表中查询名称(税前税后)
							String serviceTypeName = HRATCommonUtils.queryNameByDefdocPk(serviceType, "劳务费类型");
							// 获取证件号码
							String idCardNo = (String) rows[i].getValue(dataset.nameToIndex("defitem42"));
							// 获取页面性别
							String pageSex = (String) rows[i].getValue(dataset.nameToIndex("defitem40"));
							// String pageSexName=
							// HRATCommonUtils.queryNameByDefdocPk(pageSex,
							// "性别");
							// 获取页面出生日期
							String pageBirthDay = (String) rows[i].getValue(dataset.nameToIndex("defitem39"));
							// 得到前台填写的姓名
							String name = (String) rows[i].getValue(dataset.nameToIndex("defitem48"));
							// 获取页面证件类型
							String idType = (String) rows[i].getValue(dataset.nameToIndex("defitem43"));
							String idTypeName = HRATCommonUtils.queryNameByDefdocPk(idType, "证件类型");
							// 获取页面国籍
							String country = (String) rows[i].getValue(dataset.nameToIndex("defitem41"));
							String countryName = HRATCommonUtils.queryNameByDefdocPk(country, "国籍");

							// 获取是否现场发放
							String isLiveGive = (String) rows[i].getValue(dataset.nameToIndex("defitem36"));
							String isLiveGiveName = HRATCommonUtils.queryNameByDefdocPk(isLiveGive, "是否");

							// 校验税前税后对应应发和实发金额
							if ("税前收入".equals(serviceTypeName)) {
								if (HRATCommonUtils.isEmpty(amount) || amount.toDouble() == 0) {
									errMsg.append("当前劳务费类型为税前收入,应发金额不能为空\n");
									flag = false;
								}
							} else if ("税后收入".equals(serviceTypeName)) {
								if (HRATCommonUtils.isEmpty(defitem44) || defitem44.toDouble() == 0) {
									errMsg.append("当前劳务费类型为税后收入,实发金额不能为空\n");
									flag = false;
								}
							}
							// 校验证件信息是否合法
							boolean judgeIDNumberIsLegal = HRATCommonUtils.judgeIDNumberIsLegal(idTypeName, idCardNo);
							boolean isIdOjbk = IdCardUtil.isValidatedAllIdcard(idCardNo);
							if (!judgeIDNumberIsLegal||!isIdOjbk) {
								errMsg.append("证件信息不合法!\n");
								flag = false;
							}else{
								// 根据身份证获取性别
								String sex = IdCardUtil.getSexFromidCardNo(idCardNo);
								// 1001N610000000002I8R
								// 1001N610000000002I8T
								// 页面性别和生成性别不匹配
								if (HRATCommonUtils.isNotEmpty(sex)) {
									if (!sex.equals(pageSex)) {
										errMsg.append("性别与身份证信息不匹配\n");
										flag = false;
									}
								}
								// 根据身份证获取出生日期
								String birthDay = IdCardUtil.getBirthDayFromidCardNo(idCardNo);
								if (HRATCommonUtils.isNotEmpty(birthDay)) {
									if (!birthDay.equals(pageBirthDay)) {
										errMsg.append("出生日期与身份证信息不匹配\n");
										flag = false;
									}
								}
							}

							IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
							SqlBuilder sql = new SqlBuilder();
							sql.append("select name, id from BD_PSNDOC where PK_PSNDOC in(select PK_PSNDOC from HI_PSNJOB where PK_PSNCL in (select pk_psncl from BD_PSNCL where NAME in ('实名制', '代理制', '派遣制', '企业自聘', '转企改制', '其他', '内退')))");
							sql.append(" and name='");
							sql.append(name);
							sql.append("'");
							sql.append(" and id='");
							sql.append(idCardNo);
							sql.append("'");
							sql.append(" and enablestate=2"); // 启用状态 是否启用
							sql.append(" and PK_ORG =(select PK_ORG from org_orgs where  name = '湖北广播电视台台本级')");// 台本级校验
							try {
								Object result = queryBS.executeQuery(sql.toString(), new ColumnProcessor());
								// 如果数据库中能够查询到数据,说明改用户不符合
								if (result != null) {
									errMsg.append("当前行人员是在职人员,不允许报销劳务费,请核对后填写!\n");
									flag = false;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						
							// 校验国籍和证件类型是否匹配(身份证->中国)
							if ("身份证".equals(idTypeName)) {
								if (!"中国".equals(countryName)) {
									errMsg.append("证件类型和国籍不匹配\n");
									flag = false;
								}
							}

							// 校验是否现场发放,如果否,则银行类别、开户银行、银行卡号需要设置为必填
							if ("否".equals(isLiveGiveName)) {
								// 获取银行类别
								String bankType = (String) rows[i].getValue(dataset.nameToIndex("defitem35"));
								if (HRATCommonUtils.isEmpty(bankType)) {
									errMsg.append("银行类别不能为空!\n");
									flag = false;
								}
								// 开户银行
								String bank = (String) rows[i].getValue(dataset.nameToIndex("defitem34"));
								if (HRATCommonUtils.isEmpty(bank)) {
									errMsg.append("开户银行不能为空!\n");
									flag = false;
								}
								// 银行卡号
								String bankNum = (String) rows[i].getValue(dataset.nameToIndex("defitem33"));
								if (HRATCommonUtils.isEmpty(bankNum)) {
									errMsg.append("银行卡号不能为空!\n");
									flag = false;
								}
							}
							
							/**
							 * 检验唯一性
							 * 同一个人在同一张报销单上不能存在两行和两行以上的劳务费报销单（收支项目为劳务费）
							 */
							// 获取收支项目
							String szxmid = (String) rows[i].getValue(dataset.nameToIndex("szxmid"));
							String szxmidName=HRATCommonUtils.queryInoutbusiclassByName(szxmid);
							
							// 判断是稿费还是劳务费
							if (szxmidName != null && szxmidName.contains(laowufei)) {
								// 判断集合中是否已经存在该用户(身份证号)
								String put = laowufeiMap.put(idCardNo, (i + 1) + "");
								if (put != null) {
									//如果改用户已经存在则将flag置为false,并拼接错误信息
									flag = false;
									errMsgTmp01 = formatPartErrMsg(rows,dataset,idCardNo,errMsgTmp01,name,laowufei);
								}
							} else if (szxmidName != null && szxmidName.contains(gaofei)) {
								// 判断集合中是否已经存在该用户(身份证号)
								String put = gaofeiMap.put(idCardNo, (i + 1 + ""));
								if (put != null) {
									//如果改用户已经存在则将flag置为false,并拼接错误信息
									flag = false;
									errMsgTmp02 = formatPartErrMsg(rows,dataset,idCardNo,errMsgTmp02,name,gaofei);
								}
							}

							// 如果该行没有错误,则剔除一开始拼接的行号
							if (flag) {
								int end = errMsg.toString().length();
								int start = end - rowStr.length();
								StringBuilder delete = errMsg.delete(start, end);
								errMsg = delete;
							}
						}
						errMsg.append(errMsgTmp01).append("\n").append(errMsgTmp02);
					}
				}
			}
		}

		return errMsg.toString();
	}
	
	/**
	 * 拼接报错信息
	 */
	private StringBuilder formatPartErrMsg(Row[] rows,Dataset dataset, String idCardNO, StringBuilder errMsgTmp, String name, String type) {
		List<String> tmpRows = new ArrayList<>();
		for (int j = 0; j < rows.length; j++) {
			// 获取证件号码
			String defitem42Tmp = (String) rows[j].getValue(dataset.nameToIndex("defitem42"));
			String szxmidTmp =HRATCommonUtils.queryInoutbusiclassByName((String) rows[j].getValue(dataset.nameToIndex("szxmid"))) ;
			if (idCardNO != null && idCardNO.equals(defitem42Tmp) && szxmidTmp != null && szxmidTmp.contains(type)) {
				tmpRows.add((j + 1) + "");
			}
		}
		String stringFormat = stringFormat(tmpRows);
		if (!errMsgTmp.toString().contains(stringFormat)) {
			errMsgTmp.append(stringFormat).append("\n");
		}
		if (!errMsgTmp.toString().contains(idCardNO)) {
			errMsgTmp.append(name + "(证件号:" + idCardNO + ")" + "\n存在多行" + type + "报销\n");
		}
		return errMsgTmp; 
	}
	
	/**
	 * 格式化list集合成string类型
	 * @param tmpRows
	 * @return
	 */
	private String stringFormat(List<String> tmpRows) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : tmpRows) {
			stringBuilder.append("第").append(string).append("行 ");
		}
		return stringBuilder.toString();
	}
	
	
	
	

	

	/**
	 * 再做计算，给界面上传值
	 * 
	 * @param BodyData
	 */
	public void caculate() {
		// 得到表头行对象
		Row headRow = headData.getSelectedRow();
		// 应发合计
		UFDouble total_yf = new UFDouble(0);
		// 实发合计
		UFDouble total_defitem44 = new UFDouble(0);
		// 税金合计
		UFDouble tax_total = new UFDouble(0);
		// 获取表头日期
		String headDate = headRow.getValue(headData.nameToIndex("djrq")).toString().substring(0, 7);
		// 错误信息收集
		StringBuffer errorMsg = new StringBuffer();

		// 循环表体行,计算本次预计扣税以及增补扣税，累加 税金合计、实发合计
		for (int i = 0; i < rows.length; i++) {
			// 应发金额 amount
			UFDouble amount = rows[i].getValue(dataset.nameToIndex("amount")) == null ? new UFDouble(0) 
					: new UFDouble(rows[i].getValue(dataset.nameToIndex("amount")).toString());
			// 实发金额 defitem44
			UFDouble defitem44 = rows[i].getValue(dataset.nameToIndex("defitem44")) == null ? new UFDouble(0)
					: new UFDouble(rows[i].getValue(dataset.nameToIndex("defitem44")).toString());
			// 获取劳务费类型主键
			String serviceType = (String) rows[i].getValue(dataset.nameToIndex("defitem50"));
			// 劳务费类型对照转换
			String serviceTypeName = HRATCommonUtils.queryNameByDefdocPk(serviceType, "劳务费类型");
			// 获取证件号码
			String idCardNO = (String) rows[i].getValue(dataset.nameToIndex("defitem42"));
			// 获取收支项目主键
			String incomeType = (String) rows[i].getValue(dataset.nameToIndex("szxmid"));
			// 收支项目类型对照转换
			String incomeTypeName = HRATCommonUtils.queryInoutbusiclassByName(incomeType);

			// 调用方法计算历史的累计应发合计，以及历史的本次预计扣税和增补扣税的合计
			Map<String,ServiceFeeCalcVO> map = ServiceFeeTaxUtils.getFeeMapByIDCardNO(idCardNO, headDate);
			ServiceFeeCalcVO servicefeecalcVO = map.get(idCardNO);
			
			// 历史累计应发
			UFDouble historyLjyf = servicefeecalcVO.getLjyf();
			// 历史的本次预计扣税+增补扣税
			UFDouble historyTaxTotal = servicefeecalcVO.getZbks();
			
			// 根据应发算出的实发/根据实发算出的应发
			UFDouble res = new UFDouble();
			// 增补扣税
			UFDouble zbks = null;
			// 累计应发
			UFDouble ljyf = new UFDouble();
			// 根据累计应发得到的税额
			UFDouble afterTax = null;
			// 本次预计扣税
			UFDouble currentTax = new UFDouble();
			if (HRATCommonUtils.isNotEmpty(serviceType) && HRATCommonUtils.isNotEmpty(incomeType)) {
				// 根据劳务费类型判断
				if (serviceTypeName.equals("税前收入")) {
					// 税前
					// 判断收支项目类型
					if (incomeTypeName.contains("劳务费")) {
						// 根据应发算实发
						res = new UFDouble(ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(amount.doubleValue()));
						// 本次预计扣税
						currentTax = amount.sub(res);
						
						if(historyLjyf.compareTo(new UFDouble(0)) == 0){
							// 通过历史数据计算出来的累计应发为0的话，则当前行累计应发就等于应发，并且增补扣税等于0
							ljyf = amount;
							zbks = new UFDouble(0);
						}else{
							ljyf = historyLjyf.add(amount);
							// 根据累计应发计算出来的税
							afterTax = ljyf.sub(ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(ljyf.toDouble()));
							// 本次增补扣税 = 本次预计扣税-（历史的本次预计扣税+历史的增补扣税）
							if(afterTax.compareTo(historyTaxTotal.add(currentTax)) > 0){
								zbks = afterTax.sub(historyTaxTotal).sub(currentTax);
							}else{
								zbks = new UFDouble(0);
								errorMsg.append("当前行报销人的历史扣税计算有误导致当前增补扣税计算失败，请联系管理员处理  ");
							}
						}
						
					} else if (incomeTypeName.contains("稿费")) {
						// 根据应发算实发
						res = new UFDouble(ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(amount.doubleValue()));
						// 本次预计扣税
						currentTax = amount.sub(res);
						
						if(historyLjyf.compareTo(new UFDouble(0)) == 0){
							// 通过历史数据计算出来的累计应发为0的话，则当前行累计应发就等于应发，并且增补扣税等于0
							ljyf = amount;
							zbks = new UFDouble(0);
						}else{
							ljyf = historyLjyf.add(amount);
							// 根据累计应发计算出来的税
							afterTax = ljyf.sub(ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(ljyf.toDouble()));
							// 本次增补扣税 = 本次预计扣税-（历史的本次预计扣税+历史的增补扣税）
							if(afterTax.compareTo(historyTaxTotal.add(currentTax)) > 0){
								zbks = afterTax.sub(historyTaxTotal).sub(currentTax);
							}else{
								zbks = new UFDouble(0);
								errorMsg.append("当前行报销人的历史扣税计算有误导致当前增补扣税计算失败，请联系管理员处理  ");
							}
						}
					}
					// 实发金额
					ExpUtil.setRowValue(rows[i], BodyData, "defitem44", res.toString());
					// 增补扣税
					ExpUtil.setRowValue(rows[i], BodyData, "defitem45", zbks.toString());
					// 本次预计扣税
					ExpUtil.setRowValue(rows[i], BodyData, "defitem46", currentTax.toString());
					// 累计应发  defitem47
					ExpUtil.setRowValue(rows[i], BodyData, "defitem47", ljyf.toString());
					
				} else if (serviceTypeName.equals("税后收入")) {
					// 税后
					if (incomeTypeName.contains("劳务费")) {
						// 根据实发算应发
						res = new UFDouble(ServiceFeeTaxUtils.serviceFeeCaculatePreTaxByAfterTax(defitem44.doubleValue()));
						// // 本次预计扣税
						currentTax = res.sub(defitem44);
						if(historyLjyf.compareTo(new UFDouble(0)) == 0){
							// 通过历史数据计算出来的累计应发为0的话，则当前行累计应发就等于应发，并且增补扣税等于0
							ljyf = res;
							zbks = new UFDouble(0);
						}else{
							ljyf = historyLjyf.add(res);
							// 根据累计应发计算出来的税
							afterTax = ljyf.sub(ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(ljyf.toDouble()));
							// 本次增补扣税 = 本次预计扣税-（历史的本次预计扣税+历史的增补扣税）
							if(afterTax.compareTo(historyTaxTotal.add(currentTax)) > 0){
								zbks = afterTax.sub(historyTaxTotal).sub(currentTax);
							}else{
								zbks = new UFDouble(0);
								errorMsg.append("当前行报销人的历史扣税计算有误导致当前增补扣税计算失败，请联系管理员处理  ");
							}
						}
						
					} else if (incomeTypeName.contains("稿费")) {
						res = new UFDouble(ServiceFeeTaxUtils.airticleFeeCaculatePreTaxByAfterTax(defitem44.doubleValue()));
						// 本次预计扣税
						currentTax = res.sub(defitem44);
						
						if(historyLjyf.compareTo(new UFDouble(0)) == 0){
							// 通过历史数据计算出来的累计应发为0的话，则当前行累计应发就等于应发，并且增补扣税等于0
							ljyf = res;
							zbks = new UFDouble(0);
						}else{
							ljyf = historyLjyf.add(res);
							// 根据累计应发计算出来的税
							afterTax = ljyf.sub(ServiceFeeTaxUtils.airticleFeeCaculatePreTaxByAfterTax(ljyf.toDouble()));
							// 本次增补扣税 = 本次预计扣税-（历史的本次预计扣税+历史的增补扣税）
							if(afterTax.compareTo(historyTaxTotal.add(currentTax)) > 0){
								zbks = afterTax.sub(historyTaxTotal).sub(currentTax);
							}else{
								zbks = new UFDouble(0);
								errorMsg.append("当前行报销人的历史扣税计算有误导致当前增补扣税计算失败，请联系管理员处理  ");
							}
						}
					}
					// 实发金额
					ExpUtil.setRowValue(rows[i], BodyData, "amount", res.toString());
					// 增补扣税
					ExpUtil.setRowValue(rows[i], BodyData, "defitem45", zbks.toString());
					// 本次预计扣税
					ExpUtil.setRowValue(rows[i], BodyData, "defitem46", currentTax.toString());
					// 累计应发  defitem47
					ExpUtil.setRowValue(rows[i], BodyData, "defitem47", ljyf.toString());
				}
			}
			
			
			if(HRATCommonUtils.isNotEmpty(defitem44) && defitem44.compareTo(new UFDouble(0)) != 0){
				// 累加实发合计
				total_defitem44 = total_defitem44.add(defitem44);
			}else{
				// 累加实发合计
				total_defitem44 = total_defitem44.add(res);
			}
			
			if(HRATCommonUtils.isNotEmpty(amount) && amount.compareTo(new UFDouble(0)) != 0){
				// 累加应发合计
				total_yf = total_yf.add(amount);
			}else{
				// 累加应发合计
				total_yf = total_yf.add(res);
			}
			
			// 累加每一行的税金合计
			tax_total = tax_total.add(currentTax).add(zbks);
		}
		
		
		// 给系统预制字段强行赋值
		ExpUtil.setRowValue(headRow, headData, "total", total_yf);
		ExpUtil.setRowValue(headRow, headData, "ybje", total_yf);
		
		// zyx28 表头的实发合计 = 表体的实发金额相加
		ExpUtil.setRowValue(headRow, headData, "zyx28",total_defitem44.toString());
		// zyx29 表头的税金合计 = 表体的本次预计扣税（defitem46）+增补扣税(defitem45)
		ExpUtil.setRowValue(headRow, headData, "zyx29", tax_total.toString());
		
		if(HRATCommonUtils.isNotEmpty(errorMsg)){
			AppInteractionUtil.showErrorDialog(errorMsg.toString(), "警告", "确认");
		}
	}


	
	
	
	
	
}
