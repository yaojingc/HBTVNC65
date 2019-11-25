package nc.ui.erm.billpub.view.eventhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.erm.util.CacheUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.arap.pub.IBxUIControl;
import nc.itf.bd.psnbankacc.IPsnBankaccPubService;
import nc.itf.fi.pub.Currency;
import nc.pubitf.uapbd.ICustomerPubService;
import nc.pubitf.uapbd.ISupplierPubService;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.model.CustBankaccDefaultRefModel;
import nc.ui.bd.ref.model.FreeCustRefModel;
import nc.ui.bd.ref.model.PsnbankaccDefaultRefModel;
import nc.ui.bd.ref.model.PsndocDefaultRefModel;
import nc.ui.er.util.BXUiUtil;
import nc.ui.erm.billpub.action.HRATCommonUtils;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.remote.UserBankAccVoCall;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.erm.costshare.common.ErmForCShareUiUtil;
import nc.ui.erm.util.ErUiUtil;
import nc.ui.org.ref.DeptDefaultRefModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModelCellEditableController;
import nc.vo.arap.bx.util.BodyEditVO;
import nc.vo.arap.bx.util.ControlBodyEditVO;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.er.exception.ExceptionHandler;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class InitBodyEventHandle implements BillEditListener2, BillEditListener {
	private ErmBillBillForm editor = null;
	private EventHandleUtil eventUtil = null;
	private BodyEventHandleUtil bodyEventHandleUtil = null;
	
	// 湖北广电 劳务费报销单 新增的表体编辑后事件
	private BodyAfterEditHandler bodyAfterEditHandler = null;
	

	public InitBodyEventHandle(ErmBillBillForm editor) {
		this.editor = editor;
		this.eventUtil = new EventHandleUtil(editor);
		this.bodyEventHandleUtil = new BodyEventHandleUtil(editor);
		// 给新增的表体编辑后事件赋值
		this.bodyAfterEditHandler = new BodyAfterEditHandler(editor);
	}

	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		String fydwbm = this.bodyEventHandleUtil.getHeadItemStrValue("fydwbm");
		if (e.getTableCode().equalsIgnoreCase("er_cshare_detail")) {
			ErmForCShareUiUtil.doCShareBeforeEdit(e, getBillCardPanel());
		} else if ("szxmid".equals(key)) {
			UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
					e.getTableCode(), key);
			refPane.getRefModel().setUseDataPower(true);
			refPane.setPk_org(fydwbm);
		} else if ("pk_resacostcenter".equals(key)) {
			UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
					e.getTableCode(), key);
			String pk_pcorg = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "pk_pcorg", e.getTableCode());
			if (pk_pcorg == null) {
				refPane.setEnabled(false);
			} else {
				refPane.setEnabled(true);
				String wherePart = "pk_profitcenter='" + pk_pcorg + "'";
				this.bodyEventHandleUtil.addWherePart2RefModel(refPane,
						pk_pcorg, wherePart);
			}
		} else if ("pk_checkele".equals(key)) {
			UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
					e.getTableCode(), key);
			String pk_pcorg = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "pk_pcorg", e.getTableCode());
			if (pk_pcorg != null) {
				refPane.setEnabled(true);
				this.bodyEventHandleUtil.setPkOrg2RefModel(refPane, pk_pcorg);
			} else {
				refPane.setEnabled(false);
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "pk_pcorg");
			}
		} else if ("projecttask".equals(key)) {
			String pk_project = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "jobid", e.getTableCode());
			UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
					e.getTableCode(), key);
			if (pk_project != null) {
				String wherePart = " pk_project='" + pk_project + "'";

				String pkOrg = this.bodyEventHandleUtil
						.getBodyItemUIRefPane(e.getTableCode(), "jobid")
						.getRefModel().getPk_org();
				String pk_org = this.bodyEventHandleUtil
						.getHeadItemStrValue("fydwbm");
				if (BXUiUtil.getPK_group().equals(pkOrg)) {
					pk_org = BXUiUtil.getPK_group();
				}

				refPane.setEnabled(true);
				this.bodyEventHandleUtil.setWherePart2RefModel(refPane, pk_org,
						wherePart);
			} else {
				refPane.setPK(null);
				refPane.setEnabled(false);
			}
		} else if ("deptid".equals(key)) {
			String dwbm = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "dwbm", e.getTableCode());
			if (dwbm == null) {
				dwbm = this.bodyEventHandleUtil.getHeadItemStrValue("dwbm");
			}
			UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
					e.getTableCode(), key);
			if (dwbm != null) {
				refPane.setEnabled(true);
				DeptDefaultRefModel model = (DeptDefaultRefModel) refPane
						.getRefModel();
				model.setPk_org(dwbm);
			} else {
				refPane.setEnabled(false);
			}
		} else if ("jkbxr".equals(key)) {
			String dwbm = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "dwbm", e.getTableCode());
			if (dwbm == null) {
				dwbm = this.bodyEventHandleUtil.getHeadItemStrValue("dwbm");
			}
			UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
					e.getTableCode(), key);
			if (dwbm != null) {
				refPane.setEnabled(true);
				PsndocDefaultRefModel model = (PsndocDefaultRefModel) refPane
						.getRefModel();
				model.setPk_org(dwbm);
			} else {
				refPane.setEnabled(false);
			}

			BillItem billItem = this.editor.getBillCardPanel().getBodyItem(
					e.getTableCode(), key);
			ErUiUtil.filterLeavePowerShowAndUI(billItem, false);
		} else if ("receiver".equals(key)) {
			String dwbm = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "dwbm", e.getTableCode());
			BillItem item = this.editor.getBillCardPanel().getBodyItem(
					e.getTableCode(), "dwbm");
			UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
					e.getTableCode(), key);
			if (dwbm != null) {
				refPane.setEnabled(true);
				PsndocDefaultRefModel model = (PsndocDefaultRefModel) refPane
						.getRefModel();
				model.setPk_org(dwbm);
			} else if (!item.isShow()) {
				BillItem hitem = this.editor.getBillCardPanel().getHeadItem(
						"dwbm");
				if ((hitem != null) && (hitem.getValueObject() != null)) {
					refPane.setEnabled(true);
					PsndocDefaultRefModel model = (PsndocDefaultRefModel) refPane
							.getRefModel();
					model.setPk_org(hitem.getValueObject().toString());
				} else {
					refPane.setEnabled(false);
				}
			} else {
				refPane.setEnabled(false);
			}

			BillItem billItem = this.editor.getBillCardPanel().getBodyItem(
					e.getTableCode(), key);
			ErUiUtil.filterLeavePowerShowAndUI(billItem, false);
		} else if ("skyhzh".equals(key)) {
			beforeSkyhzhFilter(e, key);
		} else if (("hbbm".equals(key)) || ("customer".equals(key))) {
			beforeHbbmAndCustomer(e, key, fydwbm);
		} else if ("custaccount".equals(key)) {
			beforeCustAccount(e, key);
		} else if ("freecust".equals(key)) {
			beforeEditFreecust(e);
		} else if ("fctno".equals(key)) {
			String pk_org = this.bodyEventHandleUtil
					.getHeadItemStrValue("pk_org");
			beforeFct(e, key, pk_org);
		} else if ((key != null) && (key.startsWith("defitem"))) {
			filterDefItemField(key);
		}
		try {
			CrossCheckUtil.checkRule("N", key, this.editor);
		} catch (BusinessException e1) {
			ExceptionHandler.handleExceptionRuntime(e1);
			return false;
		}

		return this.editor.getErmEventTransformer().beforeEdit(e);
	}

	private void beforeEditFreecust(BillEditEvent e) {
		String pk_custsup = null;
		Integer paytarget = (Integer) getBodyItemValue(e.getTableCode(),
				e.getRow(), "paytarget");
		if (paytarget == null) {
			paytarget = (Integer) getHeadValue("paytarget");
			if (paytarget == null) {
				paytarget = Integer.valueOf(0);
			}
		}

		if (paytarget.intValue() == 1) {
			pk_custsup = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "hbbm", e.getTableCode());
		} else if (paytarget.intValue() == 2) {
			pk_custsup = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "customer", e.getTableCode());
		} else {
			String hbbm = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "hbbm", e.getTableCode());
			if (hbbm != null)
				pk_custsup = hbbm;
			else {
				pk_custsup = this.bodyEventHandleUtil.getBodyItemStrValue(
						e.getRow(), "customer", e.getTableCode());
			}
		}

		UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
				e.getTableCode(), "freecust");
		((FreeCustRefModel) refPane.getRefModel())
				.setCustomSupplier(pk_custsup);
	}

	private void beforeCustAccount(BillEditEvent e, String key) {
		UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
				e.getTableCode(), key);
		int accclass = 3;
		String pk_custsup = null;
		Integer paytarget = (Integer) getBodyItemValue(e.getTableCode(),
				e.getRow(), "paytarget");
		if (paytarget == null) {
			paytarget = (Integer) getHeadValue("paytarget");
			if (paytarget == null) {
				paytarget = Integer.valueOf(0);
			}
		}

		if (paytarget.intValue() == 1) {
			accclass = 3;
			pk_custsup = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "hbbm", e.getTableCode());
		} else if (paytarget.intValue() == 2) {
			accclass = 1;
			pk_custsup = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "customer", e.getTableCode());
		}

		if (isReceiverPaytarget(e)) {
			pk_custsup = null;
		}

		String freecust = this.bodyEventHandleUtil.getBodyItemStrValue(
				e.getRow(), "freecust", e.getTableCode());
		if (freecust != null) {
			pk_custsup = null;
		}

		String pk_currtype = this.bodyEventHandleUtil
				.getHeadItemStrValue("bzbm");

		StringBuffer wherepart = new StringBuffer();
		wherepart.append(" pk_currtype='" + pk_currtype + "'");
		wherepart.append(" and enablestate='2'");
		wherepart.append(" and accclass='" + accclass + "'");
		HeadFieldHandleUtil.setWherePart2RefModel(refPane, null,
				wherepart.toString());
		if ((refPane.getRefModel() != null)
				&& ((refPane.getRefModel() instanceof CustBankaccDefaultRefModel))) {
			CustBankaccDefaultRefModel refModel = (CustBankaccDefaultRefModel) refPane
					.getRefModel();
			if (refModel != null) {
				refModel.setCustSupBankAllShow(true);
				refModel.setPk_cust(pk_custsup);
			}
		}
	}

	private void beforeFct(BillEditEvent e, String key, String pk_org) {
		UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
				e.getTableCode(), key);
		if (pk_org != null) {
			refPane.setEnabled(true);
			AbstractRefModel model = refPane.getRefModel();
			if (model != null)
				model.setPk_org(pk_org);
		} else {
			refPane.setEnabled(false);
		}
	}

	private void beforeHbbmAndCustomer(BillEditEvent e, String key,
			String fydwbm) {
		UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
				e.getTableCode(), key);
		if (fydwbm != null) {
			AbstractRefModel model = refPane.getRefModel();
			if (model != null)
				model.setPk_org(fydwbm);
		}
	}

	private void beforeSkyhzhFilter(BillEditEvent e, String key) {
		UIRefPane refPane = this.bodyEventHandleUtil.getBodyItemUIRefPane(
				e.getTableCode(), key);
		if (isReceiverPaytarget(e)) {
			String receiver = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "receiver", e.getTableCode());
			String pk_currtype = (String) this.editor.getBillCardPanel()
					.getHeadItem("bzbm").getValueObject();
			StringBuffer wherepart = new StringBuffer();
			wherepart.append(" pk_psndoc='" + receiver + "'");
			wherepart.append(" and pk_currtype='" + pk_currtype + "'");
			if (receiver != null) {
				refPane.setEnabled(true);
				PsnbankaccDefaultRefModel model = (PsnbankaccDefaultRefModel) refPane
						.getRefModel();
				if (model != null) {
					model.setPk_psndoc(receiver);
					model.setWherePart(wherepart.toString());
				}
			} else {
				refPane.setEnabled(false);
			}
		} else {
			refPane.setEnabled(false);
		}
	}

	private void filterDefItemField(String key) {
		BillItem bodyItem = this.editor.getBillCardPanel().getBodyItem(key);
		if (((bodyItem.getComponent() instanceof UIRefPane))
				&& (((UIRefPane) bodyItem.getComponent()).getRefModel() != null)) {
			ErmBillBillForm ermBillFom = this.editor;
			String pk_org = null;
			if ((ermBillFom.getOrgRefFields("pk_org") != null)
					&& (ermBillFom.getOrgRefFields("pk_org").contains(key))) {
				BillItem item = ermBillFom.getBillCardPanel().getHeadItem(
						"pk_org");
				if (item != null)
					pk_org = (String) item.getValueObject();
			} else if ((ermBillFom.getOrgRefFields("dwbm") != null)
					&& (ermBillFom.getOrgRefFields("dwbm").contains(key))) {
				BillItem item = ermBillFom.getBillCardPanel().getHeadItem(
						"dwbm");
				if (item != null)
					pk_org = (String) item.getValueObject();
			} else if ((ermBillFom.getOrgRefFields("fydwbm") != null)
					&& (ermBillFom.getOrgRefFields("fydwbm").contains(key))) {
				BillItem item = ermBillFom.getBillCardPanel().getHeadItem(
						"fydwbm");
				if (item != null)
					pk_org = (String) item.getValueObject();
			} else if ((ermBillFom.getOrgRefFields(JKBXHeaderVO.PK_PAYORG) != null)
					&& (ermBillFom.getOrgRefFields(JKBXHeaderVO.PK_PAYORG)
							.contains(key))) {
				BillItem item = ermBillFom.getBillCardPanel().getHeadItem(
						JKBXHeaderVO.PK_PAYORG);
				if (item != null)
					pk_org = (String) item.getValueObject();
			} else {
				BillItem item = ermBillFom.getBillCardPanel().getHeadItem(
						"pk_org");
				if (item != null) {
					pk_org = (String) item.getValueObject();
				}
			}

			((UIRefPane) bodyItem.getComponent()).getRefModel().setPk_org(
					pk_org);
			((UIRefPane) bodyItem.getComponent()).getRefModel()
					.setDataPowerOperation_code("fi");
		}
	}

	public boolean isControlItem(BillEditEvent e, ControlBodyEditVO vo) {
		if ((e.getTableCode().equals(vo.getTablecode()))
				&& (e.getKey().equals(vo.getItemkey()))) {
			return true;
		}
		if (vo.getDimlist() != null) {
			for (BodyEditVO bvo : vo.getDimlist()) {
				if ((e.getTableCode().equals(bvo.getTablecode()))
						&& (e.getKey().equals(bvo.getItemkey())))
					return true;
			}
		}
		return false;
	}

	public void afterEdit(BillEditEvent e) {
		
		Integer row = Integer.valueOf(e.getRow());
		
		// 新增的表体编辑后事件
		bodyAfterEditHandler.doEdit(e.getKey(),row);
		
		BillItem bodyItem = getBillCardPanel().getBodyItem(e.getTableCode(),
				e.getKey());
		if (bodyItem == null) {
			return;
		}
		
		
		
		

		if (e.getTableCode().equals("er_cshare_detail")) {
			ErmForCShareUiUtil.doCShareAfterEdit(e, getBillCardPanel());
		} else {
			if (!isImporting()) {
				this.editor.doReimRuleAction();
			}

			List<ControlBodyEditVO> controlRule = this.editor.getControlRule();

			ControlBodyEditVO vo = new ControlBodyEditVO();
			for (ControlBodyEditVO controlVO : controlRule) {
				if (controlVO.getRow() == e.getRow()) {
					vo = controlVO;
				}
			}

			if ((vo != null)
					&& (getBillCardPanel().getBodyValueAt(e.getRow(),
							vo.getItemkey()) != null)) {
				int[] dcimalAndRound = Currency
						.getCurrDigitAndRoundtype(getHeadValue("bzbm")
								.toString());

				Double amount = Double.valueOf(Double
						.parseDouble(getBillCardPanel().getBodyValueAt(
								e.getRow(), vo.getItemkey()).toString()));
				Double standard = Double.valueOf(Double.parseDouble(vo
						.getValue().toString()));
				String formula = vo.getFormulaRule();

				if (formula != null) {
					if (formula.indexOf("->") < 0)
						formula = "defitem12->" + formula;
					getBillCardPanel().getBodyItem(e.getTableCode(),
							"defitem12").setEditFormula(
							new String[] { formula });
					getBillCardPanel().getBillModel().execEditFormulasByKey(
							e.getRow(), "defitem12");
					if (getBillCardPanel().getBodyValueAt(e.getRow(),
							"defitem12") != null) {
						standard = Double.valueOf(Double
								.parseDouble(getBillCardPanel().getBodyValueAt(
										e.getRow(), "defitem12").toString()));
						UFDouble tmp = new UFDouble(standard);
						standard = Double.valueOf(tmp.setScale(
								dcimalAndRound[0], dcimalAndRound[1])
								.doubleValue());
					}
				}

				
				if (amount.doubleValue() > standard.doubleValue()) {
					if (vo.getTip() == 1) {
						MessageDialog
								.showHintDlg(
										null,
										NCLangRes4VoTransl.getNCLangRes()
												.getStrByID("expensepub_0",
														"0expense-000007"),
										NCLangRes4VoTransl
												.getNCLangRes()
												.getString(
														"expensepub_0",
														null,
														"0expense-000008",
														null,
														new String[] { ""
																+ (e.getRow() + 1) }));

						this.editor.getRows().add(row);
						this.editor.getRow_pk_reimrule().put(row,
								vo.getPk_reimRule());
					} else if (vo.getTip() == 2) {
						MessageDialog
								.showHintDlg(
										null,
										NCLangRes4VoTransl.getNCLangRes()
												.getStrByID("expensepub_0",
														"0expense-000007"),
										NCLangRes4VoTransl
												.getNCLangRes()
												.getString(
														"expensepub_0",
														null,
														"0expense-000009",
														null,
														new String[] { ""
																+ (e.getRow() + 1) }));

						getBillCardPanel().setBodyValueAt(standard, e.getRow(),
								vo.getItemkey());
						getBillCardPanel().getBillModel()
								.execEditFormulasByKey(e.getRow(),
										vo.getItemkey());
						bodyItem = getBillCardPanel().getBodyItem(
								e.getTableCode(), "amount");
					}
				} else if (this.editor.getRows().contains(row)) {
					this.editor.getRows().remove(row);
				}

			}

			if ((bodyItem.getKey().equals("amount"))
					|| (isAmoutField(bodyItem))) {
				Object amount = getBillCardPanel().getBillModel(
						e.getTableCode()).getValueAt(e.getRow(), "amount");
				getBillCardPanel().getBillModel(e.getTableCode()).setValueAt(
						amount, e.getRow(), "ybje");
				try {
					if (!isImporting()) {
						calcuateHeadJe();
					}
					this.bodyEventHandleUtil.modifyFinValues("ybje",
							e.getRow(), e);
				} catch (BusinessException e1) {
					ExceptionHandler.handleExceptionRuntime(e1);
				}
			} else if ((e.getKey().equals("ybje"))
					|| (e.getKey().equals("cjkybje"))
					|| (e.getKey().equals("zfybje"))
					|| (e.getKey().equals("hkybje"))) {
				if (e.getKey().equals("ybje")) {
					try {
						if (!isImporting())
							calcuateHeadJe();
					} catch (BusinessException e1) {
						ExceptionHandler.handleExceptionRuntime(e1);
					}
				}

				this.bodyEventHandleUtil.modifyFinValues(e.getKey(),
						e.getRow(), e);
			} else if (e.getKey().equals("pk_pcorg_v")) {
				String pk_prong_v = this.bodyEventHandleUtil
						.getBodyItemStrValue(e.getRow(), e.getKey(),
								e.getTableCode());
				UIRefPane refPane = (UIRefPane) getBillCardPanel().getBodyItem(
						e.getKey()).getComponent();

				String oldid = MultiVersionUtil.getBillFinanceOrg(
						refPane.getRefModel(), pk_prong_v);
				getBillCardPanel()
						.getBillData()
						.getBillModel()
						.setValueAt(new DefaultConstEnum(oldid, "pk_pcorg"),
								e.getRow(), "pk_pcorg");
				getBillCardPanel().getBillData().getBillModel()
						.loadLoadRelationItemValue(e.getRow(), "pk_pcorg");
				afterEditPk_corp(e);
			} else if (e.getKey().equals("pk_pcorg")) {
				BillItem pcorg_vItem = getBillCardPanel().getBodyItem(
						"pk_pcorg_v");
				if (pcorg_vItem != null) {
					UFDate date = (UFDate) getBillCardPanel().getHeadItem(
							"djrq").getValueObject();
					if (date != null) {
						String pk_pcorg = this.bodyEventHandleUtil
								.getBodyItemStrValue(e.getRow(), "pk_pcorg",
										e.getTableCode());
						Map map = MultiVersionUtil.getFinanceOrgVersion(
								((UIRefPane) pcorg_vItem.getComponent())
										.getRefModel(),
								new String[] { pk_pcorg }, date);
						String vid = map.keySet().size() == 0 ? null
								: (String) map.keySet().iterator().next();
						getBillCardPanel().getBillModel().setValueAt(vid,
								e.getRow(), "pk_pcorg_v_ID");
						getBillCardPanel().getBillModel()
								.loadLoadRelationItemValue(e.getRow(),
										"pk_pcorg_v");
					}
				}
				afterEditPk_corp(e);
			} else if (e.getKey().equals("jobid")) {
				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(null, e.getRow(), "projecttask");
			} else if (e.getKey().equals("dwbm")) {
				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(null, e.getRow(), "deptid");
				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(null, e.getRow(), "jkbxr");

				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(null, e.getRow(), "receiver");
				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(null, e.getRow(), "skyhzh");
			} else if (e.getKey().equals("deptid")) {
				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(null, e.getRow(), "jkbxr");
			} else if (e.getKey().equals("paytarget")) {
				afterEditPaytarget(e);
			} else if (e.getKey().equals("jkbxr")) {
				try {
					afterEditJkbxr(e);
				} catch (BusinessException e1) {
					ExceptionHandler.handleExceptionRuntime(e1);
				}
			} else if (e.getKey().equals("receiver")) {
				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(null, e.getRow(), "skyhzh");
				setDefaultSkyhzhByReceiver(e.getTableCode(), e.getRow());
			} else if ((e.getKey().equals("hbbm"))
					|| (e.getKey().equals("customer"))) {
				Integer paytarget = getBillPayTarget(e.getRow());

				if (e.getKey().equals("hbbm")) {
					if (paytarget.compareTo(Integer.valueOf(1)) == 0) {
						getBillCardPanel().getBillData()
								.getBillModel(e.getTableCode())
								.setValueAt(null, e.getRow(), "custaccount");
						getBillCardPanel().getBillData()
								.getBillModel(e.getTableCode())
								.setValueAt(null, e.getRow(), "freecust");
						getBillCardPanel()
								.getBillData()
								.getBillModel(e.getTableCode())
								.setValueAt(null, e.getRow(),
										"freecust.bankaccount");

						setDefaultCustaccountBySupplier(e.getTableCode(),
								e.getRow());
					}
				} else if (paytarget.compareTo(Integer.valueOf(2)) == 0) {
					getBillCardPanel().getBillData()
							.getBillModel(e.getTableCode())
							.setValueAt(null, e.getRow(), "custaccount");
					getBillCardPanel().getBillData()
							.getBillModel(e.getTableCode())
							.setValueAt(null, e.getRow(), "freecust");
					getBillCardPanel()
							.getBillData()
							.getBillModel(e.getTableCode())
							.setValueAt(null, e.getRow(),
									"freecust.bankaccount");

					setDefaultCustaccountByCustomer(e.getTableCode(),
							e.getRow());
				}
			}

			if (this.bodyEventHandleUtil.getUserdefine(1, bodyItem.getKey(), 2) != null) {
				String formula = this.bodyEventHandleUtil.getUserdefine(1,
						bodyItem.getKey(), 2);
				String[] strings = formula.split(";");
				for (String form : strings) {
					this.bodyEventHandleUtil.doFormulaAction(form, e.getKey(),
							e.getRow(), e.getTableCode(), e.getValue());
				}
			}

			try {
				this.bodyEventHandleUtil.doContract(bodyItem, e);
			} catch (BusinessException e1) {
				ExceptionHandler.handleExceptionRuntime(e1);
			}
		}

		this.editor.getErmEventTransformer().afterEdit(e);
	}

	private void afterEditPaytarget(BillEditEvent e) {
		DefaultConstEnum bodyItemStrValue = (DefaultConstEnum) getBillCardPanel()
				.getBillModel().getValueObjectAt(e.getRow(), "paytarget");
		if (bodyItemStrValue != null) {
			Integer paytarget = (Integer) bodyItemStrValue.getValue();
			if (paytarget.intValue() == 1) {
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"custaccount_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"freecust_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"freecust.bankaccount", e.getTableCode());

				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"skyhzh_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"receiver_ID", e.getTableCode());

				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem39", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem38", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem37", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem36", e.getTableCode());
			} else if (paytarget.intValue() == 2) {
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"custaccount_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"freecust_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"freecust.bankaccount", e.getTableCode());

				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"skyhzh_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"receiver_ID", e.getTableCode());

				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem39", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem38", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem37", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem36", e.getTableCode());
			} else if (paytarget.intValue() == 0) {
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"custaccount_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"freecust_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"freecust.bankaccount", e.getTableCode());

				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem39", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem38", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem37", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"defitem36", e.getTableCode());
			} else if (paytarget.intValue() == 3) {
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"custaccount_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"freecust_ID", e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"freecust.bankaccount", e.getTableCode());

				getBillCardPanel().setBodyValueAt(null, e.getRow(), "skyhzh",
						e.getTableCode());
				getBillCardPanel().setBodyValueAt(null, e.getRow(),
						"receiver_ID", e.getTableCode());
			}
		}
	}

	private void setDefaultSkyhzhByReceiver(String tablecode, int row) {
		Integer payTarget = getBillPayTarget(row);
		if ((payTarget != null) && (payTarget.equals(Integer.valueOf(0)))) {
			String receiver = this.bodyEventHandleUtil.getBodyItemStrValue(row,
					"receiver", tablecode);
			try {
				String key = UserBankAccVoCall.USERBANKACC_VOCALL + receiver;
				if (WorkbenchEnvironment.getInstance().getClientCache(key) != null) {
					BankAccSubVO[] vos = (BankAccSubVO[]) WorkbenchEnvironment
							.getInstance().getClientCache(key);
					if ((vos != null) && (vos.length > 0) && (vos[0] != null))
						getBillCardPanel()
								.getBillData()
								.getBillModel(tablecode)
								.setValueAt(vos[0].getPk_bankaccsub(), row,
										"skyhzh");
				} else {
					BankAccbasVO bank = ((IPsnBankaccPubService) NCLocator
							.getInstance().lookup(IPsnBankaccPubService.class))
							.queryDefaultBankAccByPsnDoc(receiver);
					if ((bank != null) && (bank.getBankaccsub() != null)) {
						WorkbenchEnvironment.getInstance()
								.putClientCache(
										UserBankAccVoCall.USERBANKACC_VOCALL
												+ receiver,
										bank.getBankaccsub());
						getBillCardPanel()
								.getBillData()
								.getBillModel(tablecode)
								.setValueAt(
										bank.getBankaccsub()[0]
												.getPk_bankaccsub(),
										row, "skyhzh");
					}
				}
			} catch (Exception e) {
				ExceptionHandler.consume(e);
			}
		}
	}

	private void setDefaultCustaccountBySupplier(String tablecode, int row) {
		Integer payTarget = getBillPayTarget(row);
		if ((payTarget != null) && (payTarget.equals(Integer.valueOf(1)))) {
			String customer = this.bodyEventHandleUtil.getBodyItemStrValue(row,
					"hbbm", tablecode);
			ISupplierPubService service = (ISupplierPubService) NCLocator
					.getInstance().lookup(ISupplierPubService.class.getName());
			try {
				String custaccount = service.getDefaultBankAcc(customer);
				getBillCardPanel().getBillData().getBillModel(tablecode)
						.setValueAt(custaccount, row, "custaccount");
			} catch (Exception ex) {
				ExceptionHandler.handleExceptionRuntime(ex);
			}
		}
	}

	private void setDefaultCustaccountByCustomer(String tablecode, int row) {
		Integer payTarget = getBillPayTarget(row);
		if ((payTarget != null) && (payTarget.equals(Integer.valueOf(2)))) {
			String customer = this.bodyEventHandleUtil.getBodyItemStrValue(row,
					"customer", tablecode);
			ICustomerPubService service = (ICustomerPubService) NCLocator
					.getInstance().lookup(ICustomerPubService.class.getName());
			try {
				String custaccount = service.getDefaultBankAcc(customer);
				getBillCardPanel().getBillData().getBillModel(tablecode)
						.setValueAt(custaccount, row, "custaccount");
			} catch (Exception ex) {
				Log.getInstance(getClass()).error(ex);
			}
		}
	}

	private void afterEditJkbxr(BillEditEvent e) throws BusinessException {
		String bxr = this.bodyEventHandleUtil.getBodyItemStrValue(e.getRow(),
				"jkbxr", e.getTableCode());
		if (StringUtil.isEmpty(bxr)) {
			return;
		}

		PsnjobVO[] jobs = (PsnjobVO[]) CacheUtil.getVOArrayByPkArray(
				PsnjobVO.class, "PK_PSNDOC", new String[] { bxr });

		if (jobs == null) {
			IBxUIControl pd = (IBxUIControl) NCLocator.getInstance().lookup(
					IBxUIControl.class);
			jobs = pd.queryPsnjobVOByPsnPK(bxr);
		}

		if ((jobs != null) && (jobs.length > 1)) {
			String mainOrg = null;
			List orgList = new ArrayList();
			List deptList = new ArrayList();
			Map orgAndDeptMap = new HashMap();

			for (PsnjobVO vo : jobs) {
				orgList.add(vo.getPk_org());
				deptList.add(vo.getPk_dept());
				if (vo.getIsmainjob().equals(UFBoolean.TRUE)) {
					mainOrg = vo.getPk_org();
				}
				List list = (List) orgAndDeptMap.get(vo.getPk_org());
				if (list == null) {
					list = new ArrayList();
					list.add(vo.getPk_dept());
				}
				orgAndDeptMap.put(vo.getPk_org(), list);
			}

			String dwbm = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "dwbm", e.getTableCode());
			if ((dwbm == null) || (!orgList.contains(dwbm))) {
				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(mainOrg, e.getRow(), "dwbm");
			}

			String pk_deptid = this.bodyEventHandleUtil.getBodyItemStrValue(
					e.getRow(), "deptid", e.getTableCode());
			if ((pk_deptid == null) || (!deptList.contains(pk_deptid))) {
				List dept = (List) orgAndDeptMap.get(this.bodyEventHandleUtil
						.getBodyItemStrValue(e.getRow(), "dwbm",
								e.getTableCode()));
				getBillCardPanel().getBillData().getBillModel(e.getTableCode())
						.setValueAt(dept.get(0), e.getRow(), "deptid");
			}
		} else {
			String[] values = ErUiUtil.getPsnDocInfoById(bxr);
			if ((values != null) && (values.length > 0)
					&& (!StringUtil.isEmpty(values[1]))
					&& (!StringUtil.isEmpty(values[2]))) {
				getBillCardPanel().getBillModel().setValueAt(values[2],
						e.getRow(), "dwbm_ID");

				getBillCardPanel().getBillModel().setValueAt(values[1],
						e.getRow(), "deptid_ID");
			}
		}
	}

	private void afterEditPk_corp(BillEditEvent e) {
		getBillCardPanel().getBillData().getBillModel(e.getTableCode())
				.setValueAt(null, e.getRow(), "pk_checkele");
		getBillCardPanel().getBillData().getBillModel(e.getTableCode())
				.setValueAt(null, e.getRow(), "pk_resacostcenter");
	}

	private boolean isAmoutField(BillItem bodyItem) {
		String[] editFormulas = bodyItem.getEditFormulas();
		if (editFormulas == null) {
			return false;
		}
		for (String formula : editFormulas) {
			if (formula.indexOf("amount") != -1) {
				return true;
			}
		}
		return false;
	}

	public void calcuateHeadJe() throws BusinessException {
		this.editor.getHelper().calculateFinitemAndHeadTotal(this.editor);
		this.eventUtil.setHeadYFB();
	}

	protected void setHeadYfbByHead() {
		Object valueObject = getBillCardPanel().getHeadItem("ybje")
				.getValueObject();

		if ((valueObject == null)
				|| (valueObject.toString().trim().length() == 0)) {
			return;
		}
		UFDouble newYbje = new UFDouble(valueObject.toString());
		try {
			String bzbm = null;
			if (getHeadValue("bzbm") != null) {
				bzbm = getHeadValue("bzbm").toString();
			}

			UFDouble hl = null;

			UFDouble globalhl = getBillCardPanel().getHeadItem("globalbbhl")
					.getValueObject() != null ? new UFDouble(getBillCardPanel()
					.getHeadItem("globalbbhl").getValueObject().toString())
					: null;

			UFDouble grouphl = getBillCardPanel().getHeadItem("groupbbhl")
					.getValueObject() != null ? new UFDouble(getBillCardPanel()
					.getHeadItem("groupbbhl").getValueObject().toString())
					: null;

			if (getBillCardPanel().getHeadItem("bbhl").getValueObject() != null) {
				hl = new UFDouble(getBillCardPanel().getHeadItem("bbhl")
						.getValueObject().toString());
			}
			UFDouble[] je = Currency.computeYFB(this.eventUtil.getPk_org(), 1,
					bzbm, newYbje, null, null, null, hl, BXUiUtil.getSysdate());
			getBillCardPanel().setHeadItem("ybje", je[0]);
			getBillCardPanel().setHeadItem("bbje", je[2]);

			UFDouble[] money = Currency.computeGroupGlobalAmount(je[0], je[2],
					bzbm, BXUiUtil.getSysdate(), getBillCardPanel()
							.getHeadItem("pk_org").getValueObject().toString(),
					getBillCardPanel().getHeadItem("pk_group").getValueObject()
							.toString(), globalhl, grouphl);

			DjLXVO currentDjlx = ((ErmBillBillManageModel) this.editor
					.getModel()).getCurrentDjLXVO();
			if (("jk".equals(currentDjlx.getDjdl()))
					|| (this.editor.getResVO() != null)) {
				getBillCardPanel().setHeadItem("total", je[0]);
			}
			getBillCardPanel().setHeadItem("groupbbje", money[0]);
			getBillCardPanel().setHeadItem("globalbbje", money[1]);
			getBillCardPanel().setHeadItem("groupbbhl", money[2]);
			getBillCardPanel().setHeadItem("globalbbhl", money[3]);

			this.eventUtil.resetCjkjeAndYe(je[0], bzbm, hl);
		} catch (BusinessException e) {
			ExceptionHandler.handleExceptionRuntime(e);
		}
	}

	protected Object getHeadValue(String key) {
		BillItem headItem = getBillCardPanel().getHeadItem(key);
		if (headItem == null) {
			headItem = getBillCardPanel().getTailItem(key);
		}
		if (headItem == null) {
			return null;
		}
		return headItem.getValueObject();
	}

	public void bodyRowChange(BillEditEvent e) {
		if ((e.getOldrows() != null)
				&& (e.getOldrows().length != e.getRows().length))
			;
		this.editor.getErmEventTransformer().bodyRowChange(e);
	}

	public void resetJeAfterModifyRow() {
		if (!this.editor.getBillCardPanel().getCurrentBodyTableCode()
				.equals("er_cshare_detail")) {
			this.editor.getHelper().calculateFinitemAndHeadTotal(this.editor);
			try {
				this.eventUtil.resetHeadYFB();
			} catch (BusinessException e) {
				ExceptionHandler.handleExceptionRuntime(e);
			}
		} else {
			try {
				ErmForCShareUiUtil.calculateHeadTotal(getBillCardPanel());
			} catch (BusinessException e) {
				ExceptionHandler.handleExceptionRuntime(e);
			}
		}
	}

	public BodyEventHandleUtil getBodyEventHandleUtil() {
		return this.bodyEventHandleUtil;
	}

	public Object getBodyItemValue(String tableCode, int row, String key) {
		Object obj = getBillCardPanel().getBillModel(tableCode)
				.getValueObjectAt(row, key);
		if (obj == null)
			return null;
		if ((obj instanceof IConstEnum)) {
			return ((IConstEnum) obj).getValue();
		}
		return obj;
	}

	private boolean isBxBill() {
		return "bx".equals(getHeadValue("djdl"));
	}

	private boolean isReceiverPaytarget(BillEditEvent e) {
		Integer paytarget = (Integer) getBodyItemValue(e.getTableCode(),
				e.getRow(), "paytarget");
		if (paytarget == null) {
			paytarget = (Integer) getHeadValue("paytarget");
		}

		if ((paytarget == null) || (paytarget.intValue() == 0)) {
			return true;
		}
		return false;
	}

	private Integer getBillPayTarget(int row) {
		Integer result = null;
		DefaultConstEnum bodyItemStrValue = (DefaultConstEnum) getBillCardPanel()
				.getBillModel().getValueObjectAt(row, "paytarget");
		if (bodyItemStrValue != null)
			result = (Integer) bodyItemStrValue.getValue();
		else {
			result = (Integer) getHeadValue("paytarget");
		}

		return result;
	}

	private boolean isImporting() {
		return getBillCardPanel().getBillData().isImporting();
	}

	private BillCardPanel getBillCardPanel() {
		return this.editor.getBillCardPanel();
	}
}