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
 * defitem44 ����ʵ�����
 * 
 * 
 * �����ȶԽ����ϵ����������У�飬��֤����֮ǰ�����ݺϷ��� ���У�鼸����Ҫ�ֶ� �����Ϣ��
 * 
 * @author yao
 * 
 */
public class SaveToCaculateAndCheck {

	private LfwView mainWidget;

	private Dataset headData;

	private Dataset BodyData;

	// ����һ����̬ȫ�ֱ������ڵ���У�鷽����ʱ��ֻ��ʼ��һ�α�����
	private static Row[] rows;

	private static Dataset dataset;
	
	private IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);

	/**
	 * ͨ�����췽����ʼ���������ö���
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
	 * ��У������ϵ�����
	 * @param mainWidget
	 * @param headData
	 * @param BodyData
	 * @return
	 */
	public String checkData() {
		// ������Ϣ
		StringBuilder errMsg = new StringBuilder();
		// �õ�����ҳ������ݼ�
		Dataset[] datasets = mainWidget.getViewModels().getDatasets();
		
		// �����������Ϸֱ�洢 ����Ѻ͸��
		Map<String, String> laowufeiMap = new HashMap<>();
		Map<String, String> gaofeiMap = new HashMap<>();
		// ������Ϣ
		StringBuilder errMsgTmp01 = new StringBuilder();// �����
		StringBuilder errMsgTmp02 = new StringBuilder();// ���
		String laowufei="�����";
		String gaofei="���";

		for (Dataset dataset : datasets) {
			// ����ҳǩ����
			if (dataset instanceof MdDataset) {
				String dataId = dataset.getId();
				if ("busitem".equalsIgnoreCase(dataId)) {
					// �õ����еı���������
					rows = dataset.getAllRow();
					this.dataset = dataset;
					if (HRATCommonUtils.isNotEmpty(rows)) {
						// ѭ��������,���㱾��Ԥ�ƿ�˰�Լ�������˰���ۼ� ˰��ϼơ�ʵ���ϼ�
						for (int i = 0; i < rows.length; i++) {
							String rowStr = "��" + (i + 1) + "��:";
							errMsg.append(rowStr);
							Boolean flag = true;
							// ʵ����� defitem44
							UFDouble defitem44 = rows[i].getValue(dataset.nameToIndex("defitem44")) == null ? new UFDouble(0) : new UFDouble(rows[i].getValue(dataset.nameToIndex("defitem44")).toString());
							// Ӧ����� amount
							UFDouble amount = rows[i].getValue(dataset.nameToIndex("amount")) == null ? new UFDouble(0) : new UFDouble(rows[i].getValue(dataset.nameToIndex("amount")).toString());
							// ��ȡ�������������
							String serviceType = (String) rows[i].getValue(dataset.nameToIndex("defitem50"));
							// �ڶ�Ӧ���в�ѯ����(˰ǰ˰��)
							String serviceTypeName = HRATCommonUtils.queryNameByDefdocPk(serviceType, "���������");
							// ��ȡ֤������
							String idCardNo = (String) rows[i].getValue(dataset.nameToIndex("defitem42"));
							// ��ȡҳ���Ա�
							String pageSex = (String) rows[i].getValue(dataset.nameToIndex("defitem40"));
							// String pageSexName=
							// HRATCommonUtils.queryNameByDefdocPk(pageSex,
							// "�Ա�");
							// ��ȡҳ���������
							String pageBirthDay = (String) rows[i].getValue(dataset.nameToIndex("defitem39"));
							// �õ�ǰ̨��д������
							String name = (String) rows[i].getValue(dataset.nameToIndex("defitem48"));
							// ��ȡҳ��֤������
							String idType = (String) rows[i].getValue(dataset.nameToIndex("defitem43"));
							String idTypeName = HRATCommonUtils.queryNameByDefdocPk(idType, "֤������");
							// ��ȡҳ�����
							String country = (String) rows[i].getValue(dataset.nameToIndex("defitem41"));
							String countryName = HRATCommonUtils.queryNameByDefdocPk(country, "����");

							// ��ȡ�Ƿ��ֳ�����
							String isLiveGive = (String) rows[i].getValue(dataset.nameToIndex("defitem36"));
							String isLiveGiveName = HRATCommonUtils.queryNameByDefdocPk(isLiveGive, "�Ƿ�");

							// У��˰ǰ˰���ӦӦ����ʵ�����
							if ("˰ǰ����".equals(serviceTypeName)) {
								if (HRATCommonUtils.isEmpty(amount) || amount.toDouble() == 0) {
									errMsg.append("��ǰ���������Ϊ˰ǰ����,Ӧ������Ϊ��\n");
									flag = false;
								}
							} else if ("˰������".equals(serviceTypeName)) {
								if (HRATCommonUtils.isEmpty(defitem44) || defitem44.toDouble() == 0) {
									errMsg.append("��ǰ���������Ϊ˰������,ʵ������Ϊ��\n");
									flag = false;
								}
							}
							// У��֤����Ϣ�Ƿ�Ϸ�
							boolean judgeIDNumberIsLegal = HRATCommonUtils.judgeIDNumberIsLegal(idTypeName, idCardNo);
							boolean isIdOjbk = IdCardUtil.isValidatedAllIdcard(idCardNo);
							if (!judgeIDNumberIsLegal||!isIdOjbk) {
								errMsg.append("֤����Ϣ���Ϸ�!\n");
								flag = false;
							}else{
								// �������֤��ȡ�Ա�
								String sex = IdCardUtil.getSexFromidCardNo(idCardNo);
								// 1001N610000000002I8R
								// 1001N610000000002I8T
								// ҳ���Ա�������Ա�ƥ��
								if (HRATCommonUtils.isNotEmpty(sex)) {
									if (!sex.equals(pageSex)) {
										errMsg.append("�Ա������֤��Ϣ��ƥ��\n");
										flag = false;
									}
								}
								// �������֤��ȡ��������
								String birthDay = IdCardUtil.getBirthDayFromidCardNo(idCardNo);
								if (HRATCommonUtils.isNotEmpty(birthDay)) {
									if (!birthDay.equals(pageBirthDay)) {
										errMsg.append("�������������֤��Ϣ��ƥ��\n");
										flag = false;
									}
								}
							}

							IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
							SqlBuilder sql = new SqlBuilder();
							sql.append("select name, id from BD_PSNDOC where PK_PSNDOC in(select PK_PSNDOC from HI_PSNJOB where PK_PSNCL in (select pk_psncl from BD_PSNCL where NAME in ('ʵ����', '������', '��ǲ��', '��ҵ��Ƹ', 'ת�����', '����', '����')))");
							sql.append(" and name='");
							sql.append(name);
							sql.append("'");
							sql.append(" and id='");
							sql.append(idCardNo);
							sql.append("'");
							sql.append(" and enablestate=2"); // ����״̬ �Ƿ�����
							sql.append(" and PK_ORG =(select PK_ORG from org_orgs where  name = '�����㲥����̨̨����')");// ̨����У��
							try {
								Object result = queryBS.executeQuery(sql.toString(), new ColumnProcessor());
								// ������ݿ����ܹ���ѯ������,˵�����û�������
								if (result != null) {
									errMsg.append("��ǰ����Ա����ְ��Ա,�������������,��˶Ժ���д!\n");
									flag = false;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						
							// У�������֤�������Ƿ�ƥ��(���֤->�й�)
							if ("���֤".equals(idTypeName)) {
								if (!"�й�".equals(countryName)) {
									errMsg.append("֤�����ͺ͹�����ƥ��\n");
									flag = false;
								}
							}

							// У���Ƿ��ֳ�����,�����,��������𡢿������С����п�����Ҫ����Ϊ����
							if ("��".equals(isLiveGiveName)) {
								// ��ȡ�������
								String bankType = (String) rows[i].getValue(dataset.nameToIndex("defitem35"));
								if (HRATCommonUtils.isEmpty(bankType)) {
									errMsg.append("���������Ϊ��!\n");
									flag = false;
								}
								// ��������
								String bank = (String) rows[i].getValue(dataset.nameToIndex("defitem34"));
								if (HRATCommonUtils.isEmpty(bank)) {
									errMsg.append("�������в���Ϊ��!\n");
									flag = false;
								}
								// ���п���
								String bankNum = (String) rows[i].getValue(dataset.nameToIndex("defitem33"));
								if (HRATCommonUtils.isEmpty(bankNum)) {
									errMsg.append("���п��Ų���Ϊ��!\n");
									flag = false;
								}
							}
							
							/**
							 * ����Ψһ��
							 * ͬһ������ͬһ�ű������ϲ��ܴ������к��������ϵ�����ѱ���������֧��ĿΪ����ѣ�
							 */
							// ��ȡ��֧��Ŀ
							String szxmid = (String) rows[i].getValue(dataset.nameToIndex("szxmid"));
							String szxmidName=HRATCommonUtils.queryInoutbusiclassByName(szxmid);
							
							// �ж��Ǹ�ѻ��������
							if (szxmidName != null && szxmidName.contains(laowufei)) {
								// �жϼ������Ƿ��Ѿ����ڸ��û�(���֤��)
								String put = laowufeiMap.put(idCardNo, (i + 1) + "");
								if (put != null) {
									//������û��Ѿ�������flag��Ϊfalse,��ƴ�Ӵ�����Ϣ
									flag = false;
									errMsgTmp01 = formatPartErrMsg(rows,dataset,idCardNo,errMsgTmp01,name,laowufei);
								}
							} else if (szxmidName != null && szxmidName.contains(gaofei)) {
								// �жϼ������Ƿ��Ѿ����ڸ��û�(���֤��)
								String put = gaofeiMap.put(idCardNo, (i + 1 + ""));
								if (put != null) {
									//������û��Ѿ�������flag��Ϊfalse,��ƴ�Ӵ�����Ϣ
									flag = false;
									errMsgTmp02 = formatPartErrMsg(rows,dataset,idCardNo,errMsgTmp02,name,gaofei);
								}
							}

							// �������û�д���,���޳�һ��ʼƴ�ӵ��к�
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
	 * ƴ�ӱ�����Ϣ
	 */
	private StringBuilder formatPartErrMsg(Row[] rows,Dataset dataset, String idCardNO, StringBuilder errMsgTmp, String name, String type) {
		List<String> tmpRows = new ArrayList<>();
		for (int j = 0; j < rows.length; j++) {
			// ��ȡ֤������
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
			errMsgTmp.append(name + "(֤����:" + idCardNO + ")" + "\n���ڶ���" + type + "����\n");
		}
		return errMsgTmp; 
	}
	
	/**
	 * ��ʽ��list���ϳ�string����
	 * @param tmpRows
	 * @return
	 */
	private String stringFormat(List<String> tmpRows) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : tmpRows) {
			stringBuilder.append("��").append(string).append("�� ");
		}
		return stringBuilder.toString();
	}
	
	
	
	

	

	/**
	 * �������㣬�������ϴ�ֵ
	 * 
	 * @param BodyData
	 */
	public void caculate() {
		// �õ���ͷ�ж���
		Row headRow = headData.getSelectedRow();
		// Ӧ���ϼ�
		UFDouble total_yf = new UFDouble(0);
		// ʵ���ϼ�
		UFDouble total_defitem44 = new UFDouble(0);
		// ˰��ϼ�
		UFDouble tax_total = new UFDouble(0);
		// ��ȡ��ͷ����
		String headDate = headRow.getValue(headData.nameToIndex("djrq")).toString().substring(0, 7);
		// ������Ϣ�ռ�
		StringBuffer errorMsg = new StringBuffer();

		// ѭ��������,���㱾��Ԥ�ƿ�˰�Լ�������˰���ۼ� ˰��ϼơ�ʵ���ϼ�
		for (int i = 0; i < rows.length; i++) {
			// Ӧ����� amount
			UFDouble amount = rows[i].getValue(dataset.nameToIndex("amount")) == null ? new UFDouble(0) 
					: new UFDouble(rows[i].getValue(dataset.nameToIndex("amount")).toString());
			// ʵ����� defitem44
			UFDouble defitem44 = rows[i].getValue(dataset.nameToIndex("defitem44")) == null ? new UFDouble(0)
					: new UFDouble(rows[i].getValue(dataset.nameToIndex("defitem44")).toString());
			// ��ȡ�������������
			String serviceType = (String) rows[i].getValue(dataset.nameToIndex("defitem50"));
			// ��������Ͷ���ת��
			String serviceTypeName = HRATCommonUtils.queryNameByDefdocPk(serviceType, "���������");
			// ��ȡ֤������
			String idCardNO = (String) rows[i].getValue(dataset.nameToIndex("defitem42"));
			// ��ȡ��֧��Ŀ����
			String incomeType = (String) rows[i].getValue(dataset.nameToIndex("szxmid"));
			// ��֧��Ŀ���Ͷ���ת��
			String incomeTypeName = HRATCommonUtils.queryInoutbusiclassByName(incomeType);

			// ���÷���������ʷ���ۼ�Ӧ���ϼƣ��Լ���ʷ�ı���Ԥ�ƿ�˰��������˰�ĺϼ�
			Map<String,ServiceFeeCalcVO> map = ServiceFeeTaxUtils.getFeeMapByIDCardNO(idCardNO, headDate);
			ServiceFeeCalcVO servicefeecalcVO = map.get(idCardNO);
			
			// ��ʷ�ۼ�Ӧ��
			UFDouble historyLjyf = servicefeecalcVO.getLjyf();
			// ��ʷ�ı���Ԥ�ƿ�˰+������˰
			UFDouble historyTaxTotal = servicefeecalcVO.getZbks();
			
			// ����Ӧ�������ʵ��/����ʵ�������Ӧ��
			UFDouble res = new UFDouble();
			// ������˰
			UFDouble zbks = null;
			// �ۼ�Ӧ��
			UFDouble ljyf = new UFDouble();
			// �����ۼ�Ӧ���õ���˰��
			UFDouble afterTax = null;
			// ����Ԥ�ƿ�˰
			UFDouble currentTax = new UFDouble();
			if (HRATCommonUtils.isNotEmpty(serviceType) && HRATCommonUtils.isNotEmpty(incomeType)) {
				// ��������������ж�
				if (serviceTypeName.equals("˰ǰ����")) {
					// ˰ǰ
					// �ж���֧��Ŀ����
					if (incomeTypeName.contains("�����")) {
						// ����Ӧ����ʵ��
						res = new UFDouble(ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(amount.doubleValue()));
						// ����Ԥ�ƿ�˰
						currentTax = amount.sub(res);
						
						if(historyLjyf.compareTo(new UFDouble(0)) == 0){
							// ͨ����ʷ���ݼ���������ۼ�Ӧ��Ϊ0�Ļ�����ǰ���ۼ�Ӧ���͵���Ӧ��������������˰����0
							ljyf = amount;
							zbks = new UFDouble(0);
						}else{
							ljyf = historyLjyf.add(amount);
							// �����ۼ�Ӧ�����������˰
							afterTax = ljyf.sub(ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(ljyf.toDouble()));
							// ����������˰ = ����Ԥ�ƿ�˰-����ʷ�ı���Ԥ�ƿ�˰+��ʷ��������˰��
							if(afterTax.compareTo(historyTaxTotal.add(currentTax)) > 0){
								zbks = afterTax.sub(historyTaxTotal).sub(currentTax);
							}else{
								zbks = new UFDouble(0);
								errorMsg.append("��ǰ�б����˵���ʷ��˰���������µ�ǰ������˰����ʧ�ܣ�����ϵ����Ա����  ");
							}
						}
						
					} else if (incomeTypeName.contains("���")) {
						// ����Ӧ����ʵ��
						res = new UFDouble(ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(amount.doubleValue()));
						// ����Ԥ�ƿ�˰
						currentTax = amount.sub(res);
						
						if(historyLjyf.compareTo(new UFDouble(0)) == 0){
							// ͨ����ʷ���ݼ���������ۼ�Ӧ��Ϊ0�Ļ�����ǰ���ۼ�Ӧ���͵���Ӧ��������������˰����0
							ljyf = amount;
							zbks = new UFDouble(0);
						}else{
							ljyf = historyLjyf.add(amount);
							// �����ۼ�Ӧ�����������˰
							afterTax = ljyf.sub(ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(ljyf.toDouble()));
							// ����������˰ = ����Ԥ�ƿ�˰-����ʷ�ı���Ԥ�ƿ�˰+��ʷ��������˰��
							if(afterTax.compareTo(historyTaxTotal.add(currentTax)) > 0){
								zbks = afterTax.sub(historyTaxTotal).sub(currentTax);
							}else{
								zbks = new UFDouble(0);
								errorMsg.append("��ǰ�б����˵���ʷ��˰���������µ�ǰ������˰����ʧ�ܣ�����ϵ����Ա����  ");
							}
						}
					}
					// ʵ�����
					ExpUtil.setRowValue(rows[i], BodyData, "defitem44", res.toString());
					// ������˰
					ExpUtil.setRowValue(rows[i], BodyData, "defitem45", zbks.toString());
					// ����Ԥ�ƿ�˰
					ExpUtil.setRowValue(rows[i], BodyData, "defitem46", currentTax.toString());
					// �ۼ�Ӧ��  defitem47
					ExpUtil.setRowValue(rows[i], BodyData, "defitem47", ljyf.toString());
					
				} else if (serviceTypeName.equals("˰������")) {
					// ˰��
					if (incomeTypeName.contains("�����")) {
						// ����ʵ����Ӧ��
						res = new UFDouble(ServiceFeeTaxUtils.serviceFeeCaculatePreTaxByAfterTax(defitem44.doubleValue()));
						// // ����Ԥ�ƿ�˰
						currentTax = res.sub(defitem44);
						if(historyLjyf.compareTo(new UFDouble(0)) == 0){
							// ͨ����ʷ���ݼ���������ۼ�Ӧ��Ϊ0�Ļ�����ǰ���ۼ�Ӧ���͵���Ӧ��������������˰����0
							ljyf = res;
							zbks = new UFDouble(0);
						}else{
							ljyf = historyLjyf.add(res);
							// �����ۼ�Ӧ�����������˰
							afterTax = ljyf.sub(ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(ljyf.toDouble()));
							// ����������˰ = ����Ԥ�ƿ�˰-����ʷ�ı���Ԥ�ƿ�˰+��ʷ��������˰��
							if(afterTax.compareTo(historyTaxTotal.add(currentTax)) > 0){
								zbks = afterTax.sub(historyTaxTotal).sub(currentTax);
							}else{
								zbks = new UFDouble(0);
								errorMsg.append("��ǰ�б����˵���ʷ��˰���������µ�ǰ������˰����ʧ�ܣ�����ϵ����Ա����  ");
							}
						}
						
					} else if (incomeTypeName.contains("���")) {
						res = new UFDouble(ServiceFeeTaxUtils.airticleFeeCaculatePreTaxByAfterTax(defitem44.doubleValue()));
						// ����Ԥ�ƿ�˰
						currentTax = res.sub(defitem44);
						
						if(historyLjyf.compareTo(new UFDouble(0)) == 0){
							// ͨ����ʷ���ݼ���������ۼ�Ӧ��Ϊ0�Ļ�����ǰ���ۼ�Ӧ���͵���Ӧ��������������˰����0
							ljyf = res;
							zbks = new UFDouble(0);
						}else{
							ljyf = historyLjyf.add(res);
							// �����ۼ�Ӧ�����������˰
							afterTax = ljyf.sub(ServiceFeeTaxUtils.airticleFeeCaculatePreTaxByAfterTax(ljyf.toDouble()));
							// ����������˰ = ����Ԥ�ƿ�˰-����ʷ�ı���Ԥ�ƿ�˰+��ʷ��������˰��
							if(afterTax.compareTo(historyTaxTotal.add(currentTax)) > 0){
								zbks = afterTax.sub(historyTaxTotal).sub(currentTax);
							}else{
								zbks = new UFDouble(0);
								errorMsg.append("��ǰ�б����˵���ʷ��˰���������µ�ǰ������˰����ʧ�ܣ�����ϵ����Ա����  ");
							}
						}
					}
					// ʵ�����
					ExpUtil.setRowValue(rows[i], BodyData, "amount", res.toString());
					// ������˰
					ExpUtil.setRowValue(rows[i], BodyData, "defitem45", zbks.toString());
					// ����Ԥ�ƿ�˰
					ExpUtil.setRowValue(rows[i], BodyData, "defitem46", currentTax.toString());
					// �ۼ�Ӧ��  defitem47
					ExpUtil.setRowValue(rows[i], BodyData, "defitem47", ljyf.toString());
				}
			}
			
			
			if(HRATCommonUtils.isNotEmpty(defitem44) && defitem44.compareTo(new UFDouble(0)) != 0){
				// �ۼ�ʵ���ϼ�
				total_defitem44 = total_defitem44.add(defitem44);
			}else{
				// �ۼ�ʵ���ϼ�
				total_defitem44 = total_defitem44.add(res);
			}
			
			if(HRATCommonUtils.isNotEmpty(amount) && amount.compareTo(new UFDouble(0)) != 0){
				// �ۼ�Ӧ���ϼ�
				total_yf = total_yf.add(amount);
			}else{
				// �ۼ�Ӧ���ϼ�
				total_yf = total_yf.add(res);
			}
			
			// �ۼ�ÿһ�е�˰��ϼ�
			tax_total = tax_total.add(currentTax).add(zbks);
		}
		
		
		// ��ϵͳԤ���ֶ�ǿ�и�ֵ
		ExpUtil.setRowValue(headRow, headData, "total", total_yf);
		ExpUtil.setRowValue(headRow, headData, "ybje", total_yf);
		
		// zyx28 ��ͷ��ʵ���ϼ� = �����ʵ��������
		ExpUtil.setRowValue(headRow, headData, "zyx28",total_defitem44.toString());
		// zyx29 ��ͷ��˰��ϼ� = ����ı���Ԥ�ƿ�˰��defitem46��+������˰(defitem45)
		ExpUtil.setRowValue(headRow, headData, "zyx29", tax_total.toString());
		
		if(HRATCommonUtils.isNotEmpty(errorMsg)){
			AppInteractionUtil.showErrorDialog(errorMsg.toString(), "����", "ȷ��");
		}
	}


	
	
	
	
	
}
