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

		// �õ���һ��ҳǩ������
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
		 * �����ۼƺϲ���˰�����β��ɿ�˰
		 * 
		 * er_bxzb ����ѱ�������ͷ er_busitem ����ѱ���������
		 * 
		 * ��ͷ�ĵ������ڣ�djrq ��ǰҳǩ�ı���vo DjLXVO djlxVO;
		 */
		String selectBillTypeCode = ((ErmBillBillManageModel) getModel()).getSelectBillTypeCode(); // ��ȡ��������
		if (ServiceFeeTaxUtils.TRANSTYPE.equals(selectBillTypeCode)) {
			this.calculateConsolidated(billCardPanel, value);
		}

		/**
		 * ͬһ������ͬһ�ű������ϲ��ܴ������к��������ϵ�����ѱ���������֧��ĿΪ����ѣ�
		 * 
		 * ͬһ������ͬһ�ű������ϲ��ܴ������к��������ϵĸ�ѱ���������֧��ĿΪ��ѣ�
		 * 
		 * ��֧��Ŀ:szxmid ���֤��:defitem42
		 * 
		 */
		// �����У��ͨ�� �򱣴�
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
	 * ͬһ������ͬһ�ű������ϲ��ܴ������к��������ϵ�����ѱ���������֧��ĿΪ����ѣ�
	 * 
	 * ͬһ������ͬһ�ű������ϲ��ܴ������к��������ϵĸ�ѱ���������֧��ĿΪ��ѣ�
	 * 
	 * ��֧��Ŀ:szxmid ���֤��:defitem42
	 * 
	 * 
	 */

	private Boolean checkOnly(BillCardPanel billCardPanel, JKBXVO value) {
		Boolean flag = true;
		// �õ������VO����
		BXBusItemVO[] bodyVOS = value.getChildrenVO();
		CircularlyAccessibleValueObject[] allChildrenVO = value.getAllChildrenVO();
		// ��������֤��
		String idCardNO = null;
		// ����
		String name = null;
		// ��֧��Ŀ
		String szxmid = null;
		// �����������Ϸֱ�洢 ����Ѻ͸��
		Map<String, String> laowufeiMap = new HashMap<>();
		Map<String, String> gaofeiMap = new HashMap<>();
		// ������Ϣ
		StringBuilder errMsg = new StringBuilder();
		StringBuilder errMsgTmp01 = new StringBuilder();// �����
		StringBuilder errMsgTmp02 = new StringBuilder();// ���
		String laowufei="�����";
		String gaofei="���";
		for (int i = 0; i < allChildrenVO.length; i++) {
			// ��ȡ���֤����
			idCardNO = (String) bodyVOS[i].getDefitem42();
			// ��ȡ����
			name = (String) bodyVOS[i].getDefitem48();
			// ��ȡ��֧��Ŀ��ֵ
			szxmid = (String) billCardPanel.getBodyValueAt(i, "szxmid");
			// �ж��Ǹ�ѻ��������
			if (szxmid != null && szxmid.contains(laowufei)) {
				// �жϼ������Ƿ��Ѿ����ڸ��û�(���֤��)
				String put = laowufeiMap.put(idCardNO, (i + 1) + "");
				if (put != null) {
					//������û��Ѿ�������flag��Ϊfalse,��ƴ�Ӵ�����Ϣ
					flag = false;
					errMsgTmp01 = formatPartErrMsg(bodyVOS,idCardNO,errMsgTmp01,name,billCardPanel,laowufei);
				}
			} else if (szxmid != null && szxmid.contains(gaofei)) {
				// �жϼ������Ƿ��Ѿ����ڸ��û�(���֤��)
				String put = gaofeiMap.put(idCardNO, (i + 1 + ""));
				if (put != null) {
					//������û��Ѿ�������flag��Ϊfalse,��ƴ�Ӵ�����Ϣ
					flag = false;
					errMsgTmp02 = formatPartErrMsg(bodyVOS,idCardNO,errMsgTmp02,name,billCardPanel,gaofei);
				}
			}
		}
		errMsg.append(errMsgTmp01).append("\n").append(errMsgTmp02);
		if (!flag) {
			MessageDialog.showErrorDlg(null, "����", errMsg.toString());
		}
		return flag;
	}

	/**
	 * ƴ�ӱ�����Ϣ
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
	 * �����ۼ�Ӧ����������˰�� ���õ�������ȥ
	 * 
	 * @param value
	 *            ��ǰ���ŵ��ݵľۺ�VO
	 * 
	 *            �ۼ�Ӧ�� defitem47 ������˰ defitem45
	 * 
	 *            ���ݱ�ͷ�ĵ������ڣ������ͬһ�û� ���µ����еĻ������ͨ���� �����е� Ӧ�����ĺϼ�
	 * 
	 *            �����û��� ���룺kj ���ƣ����
	 */
	private void calculateConsolidated(BillCardPanel billCardPanel, JKBXVO value) {
		// �õ���ǰ���ݱ�ͷ��VO
		JKBXHeaderVO headerVO = value.getParentVO();
		// �õ���ǰ���ݱ�ͷ�ĵ�������
		UFDate billDate = headerVO.getDjrq();
		// ��ȡ���£�����ƥ���ѯ���嵱�µ�����
		String yearMonth = billDate.toString().substring(0, 7);
		// �õ������VO����
		BXBusItemVO[] bodyVOS = value.getChildrenVO();
		// ��������֤��
		String idCardNO = null;
		// �����Ӧ�����amount
		UFDouble amount = null;
		// ��ͷ˰��ϼ�=���屾��Ԥ�ƿ�˰+����������˰�ϼ�
		UFDouble headsjTotal = null;
		// ʵ���ϼ�=����ʵ�����ϼ�
		UFDouble headsfTotal = new UFDouble(0);
		// Ӧ���ϼ�
		UFDouble headyfTotal = new UFDouble(0);
		// ����Ѳ�ѯ����VO
		ServiceFeeCalcVO servicefeecalcVO = null;
		
		for (int i = 0; i < bodyVOS.length; i++) {
			// ���֤��
			idCardNO = (String) bodyVOS[i].getDefitem42();
			// Ӧ�����
			amount = bodyVOS[i].getAmount();
			// ͨ�������֤���Լ����µ����ڣ���ȡ��ʷ�ۼƵ�����
			Map<String,ServiceFeeCalcVO> map = ServiceFeeTaxUtils.getFeeMapByIDCardNO(idCardNO, yearMonth);
			servicefeecalcVO = map.get(idCardNO);
			
			// �ۼ�Ӧ�����
			UFDouble total = servicefeecalcVO.getLjyf();
			if (HRATCommonUtils.isNotEmpty(total)) {
				total = new UFDouble(total).add(amount);
				bodyVOS[i].setDefitem47(total.toString());
			} else {
				total = amount;
				bodyVOS[i].setDefitem47(total.toString());
			}
			
			
			/**
			 * ���ݱ���ʱ�Զ����㣬������ۼ�Ӧ������˰�˰���ȥ����Ԥ�ƿ�˰Ϊ������˰�� ��֧��Ŀ �� szxmid
			 * ����Ԥ�ƿ�˰��defitem46 ������˰��defitem45 ��������� ��defitem50
			 */
			// ���������
			String defitem50 = billCardPanel.getBodyValueAt(i, "defitem50").toString();
			// ��ȡ��֧��Ŀ��ֵ
			String szxmid = billCardPanel.getBodyValueAt(i, "szxmid").toString();
			// �õ�����Ԥ�ƿ�˰
			String defitem46 = (String) bodyVOS[i].getDefitem46();
			// ��ʷ�ۼƵ�������˰
			UFDouble zbks = servicefeecalcVO.getZbks();
			// ��ǰ�е�������˰
			UFDouble currzbks = null; 
			// �����ۼ�Ӧ���õ���˰��
			UFDouble afterTax = null;
			if (HRATCommonUtils.isNotEmpty(szxmid)) {
				if (total == amount) {
					zbks = new UFDouble(0);
				} else {
					if (szxmid.contains("�����")) {
						afterTax = total.sub(ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(total.doubleValue()));
						currzbks = afterTax.sub(zbks);
					} else if (szxmid.contains("���")) {
						afterTax = total.sub(ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(total.doubleValue()));
						currzbks = afterTax.sub(currzbks);
					}
				}
			}
			// ����ʱ��������˰��ֵ
			bodyVOS[i].setDefitem45(currzbks.toString());

			/**
			 * Ȼ������ۼ�Ӧ�����Լ�������˰�����¼���һ��ʵ����� ʵ�����(defitem44) = Ӧ�����(amount) -
			 * ����Ԥ�ƿ�˰(defitem46) - ������˰(defitem45)
			 */
			// ���»�ȡһ��ʵ�����
			String defitem44 = (String) bodyVOS[i].getDefitem44();

			if ("˰ǰ����".equals(defitem50)) {
				defitem44 = amount.sub(Double.parseDouble(defitem46)).sub(currzbks).toString();
				// ���¸���ʵ�����
				bodyVOS[i].setDefitem44(defitem44.toString());
			} else {
				amount = new UFDouble(currzbks.add(Double.parseDouble(defitem46))).add(Double.parseDouble(defitem44));
				// ���¸���Ӧ�����
				bodyVOS[i].setAmount(amount);
			}

			// ʵ�����ϼ�
			headsfTotal = headsfTotal.add(Double.parseDouble(defitem44));
			// ˰��ϼ�
			headsjTotal = currzbks.add(Double.parseDouble(defitem46));
			// �����Ӧ�������ۼ�
			headyfTotal = headyfTotal.add(amount);
		}

		/**
		 * ����ͷ��ʵ�����ϼ� �� ˰��ϼ� ��ֵ ˰��ϼ� zyx29 ʵ���ϼ� zyx28
		 * 
		 * �ϼƽ�� total �������� bbje ����ɱ� ybje
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