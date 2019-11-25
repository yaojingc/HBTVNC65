package nc.ui.erm.billpub.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.er.exp.cmd.ServiceFeeCalcVO;
import nc.bs.framework.common.NCLocator;
import nc.itf.arap.pub.IBxUIControl;
import nc.itf.er.reimtype.IReimTypeService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.SaveAction;
import nc.ui.uif2.editor.BillForm;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.er.reimrule.ReimRulerVO;
import nc.vo.fipub.exception.ExceptionHandler;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class BillSaveAction extends SaveAction {
	private ContrastAction contrastaction;
	private static final long serialVersionUID = -3683160678696139800L;

	public void doAction(ActionEvent e) throws Exception {
		BillCardPanel billCardPanel = ((ErmBillBillForm) getEditor()).getBillCardPanel();

		// 得到第一个页签的数据
		BillTabVO[] billTabVOs = billCardPanel.getBillData().getBillTabVOs(1);

		DjLXVO djlxVO = ((ErmBillBillManageModel) getModel()).getCurrentDjLXVO();
		if ((djlxVO != null) && ("jk".equals(djlxVO.getDjdl()))) {
			for (BillTabVO billTabVO : billTabVOs) {
				String metaDataPath = billTabVO.getMetadatapath();
				if ((metaDataPath != null) && ("jk_busitem".equals(metaDataPath))) {
					delBlankLine(billCardPanel, billTabVO.getTabcode());
				}
			}
		}

		JKBXVO value = ((ErmBillBillForm) getEditor()).getJKBXVO();

		if ("bx".equals(value.getParentVO().getDjdl())) {
			boolean isContrast = checkContrast();
			if (isContrast) {
				value = ((ErmBillBillForm) getEditor()).getJKBXVO();
			}
		}

		value.getParentVO().setDjzt(Integer.valueOf(1));
		
		validate(value);

		boolean execValidateFormulas = billCardPanel.getBillData().execValidateFormulas();
		if (!execValidateFormulas) {
			return;
		}

		if (((ErmBillBillForm) getEditor()).getResVO() != null) {
			JKBXVO vo = (JKBXVO) ((ErmBillBillForm) getEditor()).getResVO().getBusiobj();
			value.setMaheadvo(vo.getMaheadvo());
		}
		Map map;
		if (((ErmBillBillForm) getEditor()).getRows().size() > 0) {
			Set<Integer> set = ((ErmBillBillForm) getEditor()).getRows();
			map = ((ErmBillBillForm) getEditor()).getRow_pk_reimrule();
			for (Integer row : set) {
				ReimRulerVO rulerVO = getReimRule((String) map.get(row));
				if (rulerVO.getControlflag().intValue() == 2) {
					throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getString("expensepub_0", null, "0expense-000002", null, new String[] { "" + (row.intValue() + 1) }));
				}

				if (rulerVO.getControlflag().intValue() == 1) {
					int result = MessageDialog.showYesNoDlg((ErmBillBillForm) getEditor(), null, NCLangRes4VoTransl.getNCLangRes().getStrByID("expensepub_0", "0expense-000001"));

					if (result != 4) {
						return;
					}
				}
			}
		}

		/**
		 * 计算累计合并扣税金额，本次补缴扣税
		 * 
		 * er_bxzb 劳务费报销单表头 er_busitem 劳务费报销单表体
		 * 
		 * 表头的单据日期：djrq 当前页签的表体vo DjLXVO djlxVO;
		 */
		String selectBillTypeCode = ((ErmBillBillManageModel) getModel()).getSelectBillTypeCode(); // 获取交易类型
		if (ServiceFeeTaxUtils.TRANSTYPE.equals(selectBillTypeCode)) {
			this.calculateConsolidated(billCardPanel, value);
		}

		/**
		 * 同一个人在同一张报销单上不能存在两行和两行以上的劳务费报销单（收支项目为劳务费）
		 * 
		 * 同一个人在同一张报销单上不能存在两行和两行以上的稿费报销单（收支项目为稿费）
		 * 
		 * 收支项目:szxmid 身份证号:defitem42
		 * 
		 */
		// 如果表单校验通过 则保存
		if (checkOnly(billCardPanel, value)) {
			if (getModel().getUiState() == UIState.ADD)
				doAddSave(value);
			else if (getModel().getUiState() == UIState.EDIT) {
				doEditSave(value);
			}
			showSuccessInfo();
		}
	}

	/**
	 * 同一个人在同一张报销单上不能存在两行和两行以上的劳务费报销单（收支项目为劳务费）
	 * 
	 * 同一个人在同一张报销单上不能存在两行和两行以上的稿费报销单（收支项目为稿费）
	 * 
	 * 收支项目:szxmid 身份证号:defitem42
	 * 
	 * 
	 */

	private Boolean checkOnly(BillCardPanel billCardPanel, JKBXVO value) {
		Boolean flag = true;
		// 得到表体的VO数组
		BXBusItemVO[] bodyVOS = value.getChildrenVO();
		CircularlyAccessibleValueObject[] allChildrenVO = value.getAllChildrenVO();
		// 表体的身份证号
		String idCardNO = null;
		// 姓名
		String name = null;
		// 收支项目
		String szxmid = null;
		// 定义两个集合分别存储 劳务费和稿费
		Map<String, String> laowufeiMap = new HashMap<>();
		Map<String, String> gaofeiMap = new HashMap<>();
		// 报错信息
		StringBuilder errMsg = new StringBuilder();
		StringBuilder errMsgTmp01 = new StringBuilder();// 劳务费
		StringBuilder errMsgTmp02 = new StringBuilder();// 稿费
		String laowufei="劳务费";
		String gaofei="稿费";
		for (int i = 0; i < allChildrenVO.length; i++) {
			// 获取身份证号码
			idCardNO = (String) bodyVOS[i].getDefitem42();
			// 获取姓名
			name = (String) bodyVOS[i].getDefitem48();
			// 获取收支项目的值
			szxmid = (String) billCardPanel.getBodyValueAt(i, "szxmid");
			// 判断是稿费还是劳务费
			if (szxmid != null && szxmid.contains(laowufei)) {
				// 判断集合中是否已经存在该用户(身份证号)
				String put = laowufeiMap.put(idCardNO, (i + 1) + "");
				if (put != null) {
					//如果改用户已经存在则将flag置为false,并拼接错误信息
					flag = false;
					errMsgTmp01 = formatPartErrMsg(bodyVOS,idCardNO,errMsgTmp01,name,billCardPanel,laowufei);
				}
			} else if (szxmid != null && szxmid.contains(gaofei)) {
				// 判断集合中是否已经存在该用户(身份证号)
				String put = gaofeiMap.put(idCardNO, (i + 1 + ""));
				if (put != null) {
					//如果改用户已经存在则将flag置为false,并拼接错误信息
					flag = false;
					errMsgTmp02 = formatPartErrMsg(bodyVOS,idCardNO,errMsgTmp02,name,billCardPanel,gaofei);
				}
			}
		}
		errMsg.append(errMsgTmp01).append("\n").append(errMsgTmp02);
		if (!flag) {
			MessageDialog.showErrorDlg(null, "警告", errMsg.toString());
		}
		return flag;
	}

	/**
	 * 拼接报错信息
	 */
	private StringBuilder formatPartErrMsg(BXBusItemVO[] bodyVOS, String idCardNO, StringBuilder errMsgTmp, String name, BillCardPanel billCardPanel, String type) {
		List<String> tmpRows = new ArrayList<>();
		for (int j = 0; j < bodyVOS.length; j++) {
			String defitem42Tmp = (String) bodyVOS[j].getDefitem42();
			String szxmidTmp = (String) billCardPanel.getBodyValueAt(j, "szxmid");
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
	 * 计算累计应发和增补扣税并 设置到界面上去
	 * 
	 * @param value
	 *            当前整张单据的聚合VO
	 * 
	 *            累计应发 defitem47 增补扣税 defitem45
	 * 
	 *            根据表头的单据日期，表体的同一用户 当月的所有的会计审批通过的 单据中的 应发金额的合计
	 * 
	 *            流程用户组 编码：kj 名称：会计
	 */
	private void calculateConsolidated(BillCardPanel billCardPanel, JKBXVO value) {
		// 得到当前单据表头的VO
		JKBXHeaderVO headerVO = value.getParentVO();
		// 得到当前单据表头的单据日期
		UFDate billDate = headerVO.getDjrq();
		// 截取年月，用来匹配查询表体当月的数据
		String yearMonth = billDate.toString().substring(0, 7);
		// 得到表体的VO数组
		BXBusItemVO[] bodyVOS = value.getChildrenVO();
		// 表体的身份证号
		String idCardNO = null;
		// 表体的应发金额amount
		UFDouble amount = null;
		// 表头税金合计=表体本次预计扣税+表体增补扣税合计
		UFDouble headsjTotal = null;
		// 实发合计=表体实发金额合计
		UFDouble headsfTotal = new UFDouble(0);
		// 应发合计
		UFDouble headyfTotal = new UFDouble(0);
		// 劳务费查询对象VO
		ServiceFeeCalcVO servicefeecalcVO = null;
		
		for (int i = 0; i < bodyVOS.length; i++) {
			// 身份证号
			idCardNO = (String) bodyVOS[i].getDefitem42();
			// 应发金额
			amount = bodyVOS[i].getAmount();
			// 通过该身份证号以及当月的日期，获取历史累计的数据
			Map<String,ServiceFeeCalcVO> map = ServiceFeeTaxUtils.getFeeMapByIDCardNO(idCardNO, yearMonth);
			servicefeecalcVO = map.get(idCardNO);
			
			// 累计应发金额
			UFDouble total = servicefeecalcVO.getLjyf();
			if (HRATCommonUtils.isNotEmpty(total)) {
				total = new UFDouble(total).add(amount);
				bodyVOS[i].setDefitem47(total.toString());
			} else {
				total = amount;
				bodyVOS[i].setDefitem47(total.toString());
			}
			
			
			/**
			 * 单据保存时自动计算，会根据累计应发计算税额，税额减去本次预计扣税为增补扣税金额。 收支项目 ： szxmid
			 * 本次预计扣税：defitem46 增补扣税：defitem45 劳务费类型 ：defitem50
			 */
			// 劳务费类型
			String defitem50 = billCardPanel.getBodyValueAt(i, "defitem50").toString();
			// 获取收支项目的值
			String szxmid = billCardPanel.getBodyValueAt(i, "szxmid").toString();
			// 得到本次预计扣税
			String defitem46 = (String) bodyVOS[i].getDefitem46();
			// 历史累计的增补扣税
			UFDouble zbks = servicefeecalcVO.getZbks();
			// 当前行的增补扣税
			UFDouble currzbks = null; 
			// 根据累计应发得到的税额
			UFDouble afterTax = null;
			if (HRATCommonUtils.isNotEmpty(szxmid)) {
				if (total == amount) {
					zbks = new UFDouble(0);
				} else {
					if (szxmid.contains("劳务费")) {
						afterTax = total.sub(ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(total.doubleValue()));
						currzbks = afterTax.sub(zbks);
					} else if (szxmid.contains("稿费")) {
						afterTax = total.sub(ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(total.doubleValue()));
						currzbks = afterTax.sub(currzbks);
					}
				}
			}
			// 保存时给增补扣税赋值
			bodyVOS[i].setDefitem45(currzbks.toString());

			/**
			 * 然后根据累计应发，以及增补扣税，重新计算一次实发金额 实发金额(defitem44) = 应发金额(amount) -
			 * 本次预计扣税(defitem46) - 增补扣税(defitem45)
			 */
			// 重新获取一次实发金额
			String defitem44 = (String) bodyVOS[i].getDefitem44();

			if ("税前收入".equals(defitem50)) {
				defitem44 = amount.sub(Double.parseDouble(defitem46)).sub(currzbks).toString();
				// 重新更新实发金额
				bodyVOS[i].setDefitem44(defitem44.toString());
			} else {
				amount = new UFDouble(currzbks.add(Double.parseDouble(defitem46))).add(Double.parseDouble(defitem44));
				// 重新更新应发金额
				bodyVOS[i].setAmount(amount);
			}

			// 实发金额合计
			headsfTotal = headsfTotal.add(Double.parseDouble(defitem44));
			// 税金合计
			headsjTotal = currzbks.add(Double.parseDouble(defitem46));
			// 表体的应发金额的累计
			headyfTotal = headyfTotal.add(amount);
		}

		/**
		 * 给表头的实发金额合计 和 税金合计 赋值 税金合计 zyx29 实发合计 zyx28
		 * 
		 * 合计金额 total 报销本币 bbje 财务荷报 ybje
		 */
		headerVO.setZyx28(headsfTotal.toString());
		headerVO.setZyx29(headsjTotal.toString());
		headerVO.setTotal(headyfTotal);
		headerVO.setBbje(headyfTotal);
		headerVO.setYbje(headyfTotal);
	}

	private boolean checkContrast() {
		JKBXVO bxvo = ((ErmBillBillForm) getEditor()).getJKBXVO();

		boolean flag = true;
		if ((bxvo.getParentVO().getYbje() != null) && (bxvo.getParentVO().getYbje().doubleValue() >= 0.0D) && (bxvo.getParentVO().getCjkybje() != null) && (bxvo.getParentVO().getCjkybje().compareTo(UFDouble.ZERO_DBL) != 0)) {
			flag = false;
		}

		if (flag) {
			UFBoolean isNoticeContrast = ((ErmBillBillManageModel) getModel()).getCurrentDjLXVO().getIscontrast();
			if (isNoticeContrast.booleanValue()) {
				try {
					boolean hasJKD = ((IBxUIControl) NCLocator.getInstance().lookup(IBxUIControl.class)).getJKD(bxvo, bxvo.getParentVO().getDjrq(), null).size() > 0;
					if ((hasJKD) && (4 == MessageDialog.showYesNoDlg((BillForm) getEditor(), NCLangRes4VoTransl.getNCLangRes().getStrByID("2011", "UPP2011-000049"), NCLangRes4VoTransl.getNCLangRes().getStrByID("2011v61013_0", "02011v61013-0087")))) {
						getContrastaction().doAction(null);
					}
				} catch (Exception e) {
					ExceptionHandler.handleRuntimeException(e);
				}
			}
		}

		return flag;
	}

	public static void delBlankLine(BillCardPanel billCardPanel, String tableCode) {
		billCardPanel.stopEditing();
		int rowCount = billCardPanel.getRowCount(tableCode);
		List dellist = new ArrayList();
		for (int currow = 0; currow < rowCount; currow++) {
			UFDouble amount = (UFDouble) billCardPanel.getBillModel(tableCode).getValueAt(currow, "amount");
			UFDouble ybje = (UFDouble) billCardPanel.getBillModel(tableCode).getValueAt(currow, "ybje");
			UFDouble hkybje = (UFDouble) billCardPanel.getBillModel(tableCode).getValueAt(currow, "hkybje");
			UFDouble zfybje = (UFDouble) billCardPanel.getBillModel(tableCode).getValueAt(currow, "zfybje");

			if (((amount == null) || (amount.compareTo(UFDouble.ZERO_DBL) == 0)) && ((ybje == null) || (ybje.compareTo(UFDouble.ZERO_DBL) == 0)) && ((hkybje == null) || (hkybje.compareTo(UFDouble.ZERO_DBL) == 0)) && ((zfybje == null) || (zfybje.compareTo(UFDouble.ZERO_DBL) == 0))) {
				dellist.add(Integer.valueOf(currow));
			}
		}
		int[] del = new int[dellist.size()];
		for (int i = 0; i < dellist.size(); i++) {
			del[i] = ((Integer) dellist.get(i)).intValue();
		}
		billCardPanel.getBillModel(tableCode).delLine(del);
	}

	private ReimRulerVO getReimRule(String pk) {
		ReimRulerVO vo = new ReimRulerVO();
		try {
			IReimTypeService service = (IReimTypeService) NCLocator.getInstance().lookup(IReimTypeService.class);
			vo = service.queryReimRule(pk);
		} catch (BusinessException e) {
			ExceptionHandler.consume(e);
		}
		return vo;
	}

	public ContrastAction getContrastaction() {
		return this.contrastaction;
	}

	public void setContrastaction(ContrastAction contrastaction) {
		this.contrastaction = contrastaction;
	}
}