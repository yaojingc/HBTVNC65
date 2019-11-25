package nc.ui.erm.billpub.view.eventhandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.bd.psnbankacc.IPsnBankaccPubService;
import nc.pubitf.uapbd.ICustomerPubService;
import nc.pubitf.uapbd.ISupplierPubService_C;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.model.BankaccSubDefaultRefModel;
import nc.ui.bd.ref.model.CashAccountRefModel;
import nc.ui.bd.ref.model.CustBankaccDefaultRefModel;
import nc.ui.bd.ref.model.FreeCustRefModel;
import nc.ui.bd.ref.model.PsnbankaccDefaultRefModel;
import nc.ui.er.util.BXUiUtil;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.remote.UserBankAccVoCall;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.erm.billpub.view.ErmBillBillFormHelper;
import nc.ui.erm.util.ErUiUtil;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.vorg.ref.DeptVersionDefaultRefModel;
import nc.ui.vorg.ref.FinanceOrgVersionDefaultRefTreeModel;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.er.exception.ExceptionHandler;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class HeadFieldHandleUtil {
	private ErmBillBillForm editor = null;

	public HeadFieldHandleUtil(ErmBillBillForm editor) {
		this.editor = editor;
	}

	public void initProj() {
		UIRefPane refPane = getHeadItemUIRefPane("jobid");
		String pk_org = getHeadItemStrValue("fydwbm");
		if (pk_org == null)
			refPane.setEnabled(false);
		else {
			refPane.setEnabled(true);
		}
		refPane.getRefModel().setPk_org(pk_org);
	}

	public void initFreeCustBySupplier() {
		UIRefPane refPane = getHeadItemUIRefPane("freecust");

		String pk_supplier = getHeadItemStrValue("hbbm");

		if ((pk_supplier != null) && (pk_supplier.trim().length() > 0))
			try {
				SupplierVO[] supplierVO = ((ISupplierPubService_C) NCLocator
						.getInstance().lookup(ISupplierPubService_C.class))
						.getSupplierVO(new String[] { pk_supplier },
								new String[] { "isfreecust" });
				if ((supplierVO != null)
						&& (supplierVO.length != 0)
						&& (supplierVO[0].getIsfreecust()
								.equals(UFBoolean.TRUE))) {
					getBillCardPanel().getHeadItem("freecust").setEnabled(true);
					((FreeCustRefModel) refPane.getRefModel())
							.setCustomSupplier(pk_supplier);
				} else {
					getBillCardPanel().getHeadItem("freecust")
							.setEnabled(false);
				}
			} catch (BusinessException e) {
				ExceptionHandler.handleExceptionRuntime(e);
			}
		else
			getBillCardPanel().getHeadItem("freecust").setEnabled(false);
	}

	public void initFreeCustByCustomer() {
		UIRefPane refPane = getHeadItemUIRefPane("freecust");

		String customer = getHeadItemStrValue("customer");

		if ((customer != null) && (customer.trim().length() > 0))
			try {
				CustomerVO[] customerVO = ((ICustomerPubService) NCLocator
						.getInstance().lookup(ICustomerPubService.class))
						.getCustomerVO(new String[] { customer },
								new String[] { "isfreecust" });

				if ((customerVO != null)
						&& (customerVO.length != 0)
						&& (customerVO[0].getIsfreecust()
								.equals(UFBoolean.TRUE))) {
					getBillCardPanel().getHeadItem("freecust").setEnabled(true);
					((FreeCustRefModel) refPane.getRefModel())
							.setCustomSupplier(customer);
				} else {
					getBillCardPanel().getHeadItem("freecust")
							.setEnabled(false);
				}
			} catch (BusinessException e) {
				ExceptionHandler.handleExceptionRuntime(e);
			}
		else
			getBillCardPanel().getHeadItem("freecust").setEnabled(false);
	}

	public void initCashProj() {
		UIRefPane ref = getHeadItemUIRefPane("cashproj");
		String pk_org = null;
		BillItem headItem = getBillCardPanel().getHeadItem(
				JKBXHeaderVO.PK_PAYORG);
		if (headItem == null)
			pk_org = (String) getBillCardPanel().getHeadItem("pk_org")
					.getValueObject();
		else {
			pk_org = (String) headItem.getValueObject();
		}
		ref.getRefModel().setPk_org(pk_org);
		ref.getRefModel().addWherePart(" and inoutdirect = '1' ", false);
	}

	public void initPk_Checkele() {
		UIRefPane refPane = getHeadItemUIRefPane("pk_checkele");
		String pk_pcorg = getHeadItemStrValue("pk_pcorg");
		setPkOrg2RefModel(refPane, pk_pcorg);
	}

	public void beforeEditPkOrg_v(String vOrgField) {
		UFDate date = (UFDate) getBillCardPanel().getHeadItem("djrq")
				.getValueObject();
		if ((date == null) || (StringUtil.isEmpty(date.toString()))) {
			date = BXUiUtil.getBusiDate();
		}

		UIRefPane refPane = getHeadItemUIRefPane(vOrgField);
		FinanceOrgVersionDefaultRefTreeModel model = (FinanceOrgVersionDefaultRefTreeModel) refPane
				.getRefModel();
		model.setVstartdate(date);
	}

	public void beforeEditDept_v(String pk_org, String vDeptField) {
		UIRefPane refPane = getHeadItemUIRefPane(vDeptField);
		DeptVersionDefaultRefModel model = (DeptVersionDefaultRefModel) refPane
				.getRefModel();
		UFDate date = (UFDate) getBillCardPanel().getHeadItem("djrq")
				.getValueObject();
		if (date == null) {
			date = BXUiUtil.getBusiDate();
		}
		model.setVstartdate(date);
		model.setPk_org(pk_org);
	}

	public void initJkbxr() {
		if (!this.editor.isInit())
			try {
				BillItem headItem = this.editor.getBillCardPanel().getHeadItem(
						"jkbxr");
				initSqdlr(this.editor, headItem,
						((ErmBillBillManageModel) this.editor.getModel())
								.getCurrentBillTypeCode(), getBillCardPanel()
								.getHeadItem("dwbm"));
			} catch (BusinessException e) {
				Log.getInstance(getClass()).error(e);
			}
	}

	public static void initSqdlr(ErmBillBillForm editor, BillItem jkbxr,
			String billtype, BillItem dwbm) throws BusinessException {
		UFDate billDate = (UFDate) editor.getBillCardPanel()
				.getHeadItem("djrq").getValueObject();
		ErUiUtil.initSqdlr(editor, jkbxr, billtype,
				(String) dwbm.getValueObject(), billDate);
	}

	public void initResaCostCenter() {
		String pk_pcorg = getHeadItemStrValue("pk_pcorg");
		UIRefPane refPane = getHeadItemUIRefPane(JKBXHeaderVO.PK_RESACOSTCENTER);

		String wherePart = "pk_profitcenter='" + pk_pcorg + "'";
		addWherePart2RefModel(refPane, pk_pcorg, wherePart);

		if (pk_pcorg == null)
			refPane.setPK(null);
	}

	public void initSkyhzh() {
		String receiver = getHeadItemStrValue("receiver");
		if (!isReceiverPaytarget()) {
			receiver = null;
		}

		String pk_currtype = getHeadItemStrValue("bzbm");

		UIRefPane refpane = getHeadItemUIRefPane("skyhzh");
		StringBuffer wherepart = new StringBuffer();
		wherepart.append(" pk_psndoc='" + receiver + "'");
		wherepart.append(" and pk_currtype='" + pk_currtype + "'");

		PsnbankaccDefaultRefModel psnbankModel = (PsnbankaccDefaultRefModel) refpane
				.getRefModel();
		psnbankModel.setWherePart(wherepart.toString());
		psnbankModel.setPk_psndoc(receiver);
	}

	public void editReceiver() throws BusinessException {
		getBillCardPanel().setHeadItem("skyhzh", null);
		this.editor.getHelper().changeBusItemValue("skyhzh", null);

		setDefaultSkyhzhByReceiver();
	}

	public void setDefaultSkyhzhByReceiver() {
		BillItem headItem = getBillCardPanel().getHeadItem("receiver");
		String receiver = headItem == null ? null : (String) headItem
				.getValueObject();
		if (receiver == null) {
			return;
		}

		if (!isReceiverPaytarget()) {
			return;
		}

		initSkyhzh();
		try {
			String key = UserBankAccVoCall.USERBANKACC_VOCALL + receiver;
			if (WorkbenchEnvironment.getInstance().getClientCache(key) != null) {
				BankAccSubVO[] vos = (BankAccSubVO[]) WorkbenchEnvironment
						.getInstance().getClientCache(key);
				if ((vos != null) && (vos.length > 0) && (vos[0] != null)) {
					getBillCardPanel().setHeadItem("skyhzh",
							vos[0].getPk_bankaccsub());
					this.editor.getHelper().changeBusItemValue("skyhzh",
							vos[0].getPk_bankaccsub());
				}
			} else {
				BankAccbasVO bank = ((IPsnBankaccPubService) NCLocator
						.getInstance().lookup(IPsnBankaccPubService.class))
						.queryDefaultBankAccByPsnDoc(receiver);
				if ((bank != null) && (bank.getBankaccsub() != null)) {
					WorkbenchEnvironment.getInstance().putClientCache(
							UserBankAccVoCall.USERBANKACC_VOCALL + receiver,
							bank.getBankaccsub());
					getBillCardPanel().setHeadItem("skyhzh",
							bank.getBankaccsub()[0].getPk_bankaccsub());
					this.editor.getHelper().changeBusItemValue("skyhzh",
							bank.getBankaccsub()[0].getPk_bankaccsub());
				}
			}
		} catch (Exception e) {
			getBillCardPanel().setHeadItem("skyhzh", null);
		}
	}

	public void initProjTask() {
		String pk_project = getHeadItemStrValue("jobid");
		UIRefPane refPane = getHeadItemUIRefPane("projecttask");
		if (pk_project != null) {
			String wherePart = " pk_project='" + pk_project + "'";

			String pkOrg = getHeadItemUIRefPane("jobid").getRefModel()
					.getPk_org();
			String pk_org = getHeadItemStrValue("fydwbm");
			if (BXUiUtil.getPK_group().equals(pkOrg)) {
				pk_org = BXUiUtil.getPK_group();
			}

			setWherePart2RefModel(refPane, pk_org, wherePart);
		} else {
			setWherePart2RefModel(refPane, null, "1=0");
		}
	}

	public void initCustAccount() {
		String pk_custsup = null;
		int accclass = 0;
		Integer paytarget = (Integer) getHeadValue("paytarget");
		if ((paytarget != null) && (paytarget.intValue() == 1)) {
			pk_custsup = getHeadItemStrValue("hbbm");
			accclass = 3;
		} else if ((paytarget != null) && (paytarget.intValue() == 2)) {
			pk_custsup = getHeadItemStrValue("customer");
			accclass = 1;
		}

		if (isReceiverPaytarget()) {
			pk_custsup = null;
		}

		String freecust = getHeadItemStrValue("freecust");
		if (freecust != null) {
			pk_custsup = null;
		}

		UIRefPane refPane = getHeadItemUIRefPane("custaccount");
		String pk_currtype = getHeadItemStrValue("bzbm");
		StringBuffer wherepart = new StringBuffer();
		wherepart.append(" pk_currtype='" + pk_currtype + "'");
		wherepart.append(" and enablestate='2'");
		wherepart.append(" and accclass='" + accclass + "'");
		setWherePart2RefModel(refPane, null, wherepart.toString());
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

	public void initSzxm() {
		UIRefPane refPane = getHeadItemUIRefPane("szxmid");
		String pk_org = getHeadItemStrValue("fydwbm");
		refPane.setPk_org(pk_org);
	}

	public void initZy() {
		UIRefPane refPane = getHeadItemUIRefPane("zy");

		refPane.setAutoCheck(false);
	}

	public void initFkyhzh() {
		String pk_currtype = getHeadItemStrValue("bzbm");
		BillItem headItem = getBillCardPanel().getHeadItem("fkyhzh");
		if (headItem != null) {
			UIRefPane refPane = (UIRefPane) headItem.getComponent();

			BankaccSubDefaultRefModel model = (BankaccSubDefaultRefModel) refPane
					.getRefModel();
			String prefix = "pk_currtype = ";
			String pk_org = null;
			BillItem payHeadItem = getBillCardPanel().getHeadItem(
					JKBXHeaderVO.PK_PAYORG);
			if (payHeadItem == null)
				pk_org = (String) getBillCardPanel().getHeadItem("pk_org")
						.getValueObject();
			else {
				pk_org = (String) payHeadItem.getValueObject();
			}

			if (StringUtil.isEmpty(pk_currtype)) {
				model.setWherePart(" acctype not in ('1','2')");
				model.setPk_org(pk_org);
				return;
			}
			model.setPk_org(pk_org);
			model.setWherePart("pk_currtype = '" + pk_currtype
					+ "' and acctype not in ('1','2')");
		}
	}

	public void filtFkyhzh() {
		initFkyhzh();

		BillItem headItem = getBillCardPanel().getHeadItem("fkyhzh");
		if (headItem != null) {
			UIRefPane refPane = (UIRefPane) headItem.getComponent();
			BankaccSubDefaultRefModel model = (BankaccSubDefaultRefModel) refPane
					.getRefModel();

			String refPK = refPane.getRefPK();
			if (refPK != null) {
				Vector vec = model.matchPkData(refPK);
				if ((vec == null) || (vec.isEmpty())) {
					refPane.setPK(null);
				}

			}

			model.setMatchPkWithWherePart(false);
		}
	}

	public void initCashAccount() {
		String pk_currtype = getHeadItemStrValue("bzbm");
		UIRefPane refPane = (UIRefPane) getBillCardPanel().getHeadItem(
				JKBXHeaderVO.PK_CASHACCOUNT).getComponent();
		CashAccountRefModel model = (CashAccountRefModel) refPane.getRefModel();
		String prefix = "pk_moneytype=";
		String pk_org = (String) getBillCardPanel().getHeadItem(
				JKBXHeaderVO.PK_PAYORG).getValueObject();
		if (StringUtil.isEmpty(pk_currtype)) {
			model.setWherePart(null);
			model.setPk_org(pk_org);
			return;
		}
		model.setPk_org(pk_org);
		model.setWherePart("pk_moneytype='" + pk_currtype + "'");

		String refPK = refPane.getRefPK();
		if (refPK != null) {
			List pkValueList = new ArrayList();
			Vector vct = model.reloadData();
			Iterator it = vct.iterator();
			int index = model.getFieldIndex("pk_cashaccount");
			while (it.hasNext()) {
				Vector next = (Vector) it.next();
				pkValueList.add((String) next.get(index));
			}

			if (!pkValueList.contains(refPK))
				refPane.setPK(null);
		}
	}

	public static void addWherePart2RefModel(UIRefPane refPane, String pk_org,
			String addwherePart) {
		filterRefModelWithWherePart(refPane, pk_org, null, addwherePart);
	}

	public static void setWherePart2RefModel(UIRefPane refPane, String pk_org,
			String wherePart) {
		filterRefModelWithWherePart(refPane, pk_org, wherePart, null);
	}

	public static void filterRefModelWithWherePart(UIRefPane refPane,
			String pk_org, String wherePart, String addWherePart) {
		AbstractRefModel model = refPane.getRefModel();
		if (model != null) {
			model.setPk_org(pk_org);
			model.setWherePart(wherePart);
			if (addWherePart != null) {
				model.setPk_org(pk_org);
				model.addWherePart(" and " + addWherePart);
			}
		}
	}

	public String getHeadItemStrValue(String itemKey) {
		BillItem headItem = getBillCardPanel().getHeadItem(itemKey);
		return headItem == null ? null : (String) headItem.getValueObject();
	}

	protected boolean isJk() {
		DjLXVO currentDjlx = ((ErmBillBillManageModel) this.editor.getModel())
				.getCurrentDjLXVO();
		return "jk".equals(currentDjlx.getDjdl());
	}

	public UIRefPane getHeadItemUIRefPane(String key) {
		JComponent component = getBillCardPanel().getHeadItem(key)
				.getComponent();
		return (component instanceof UIRefPane) ? (UIRefPane) component : null;
	}

	private BillCardPanel getBillCardPanel() {
		return this.editor.getBillCardPanel();
	}

	public void setPkOrg2RefModel(UIRefPane refPane, String pk_org) {
		refPane.getRefModel().setPk_org(pk_org);
	}

	public boolean isReceiverPaytarget() {
		Integer paytarget = (Integer) getHeadValue("paytarget");
		if ((paytarget == null) || (paytarget.intValue() == 0)) {
			return true;
		}
		return false;
	}

	public boolean isBxBill() {
		return "bx".equals(getHeadItemStrValue("djdl"));
	}

	public Object getHeadValue(String key) {
		BillItem headItem = this.editor.getBillCardPanel().getHeadItem(key);
		if (headItem == null) {
			headItem = this.editor.getBillCardPanel().getTailItem(key);
		}
		if (headItem == null) {
			return null;
		}
		return headItem.getValueObject();
	}
}