package nc.ui.erm.billpub.view.eventhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JComponent;
import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.erm.util.CacheUtil;
import nc.bs.erm.util.ErAccperiodUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.arap.pub.IBxUIControl;
import nc.itf.bd.fundplan.IFundPlanQryService;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.SysInit;
import nc.itf.resa.costcenter.ICostCenterQueryOpt;
import nc.pubitf.uapbd.ICustomerPubService;
import nc.pubitf.uapbd.ISupplierPubService;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.model.AccPeriodDefaultRefModel;
import nc.ui.bd.ref.model.BankaccSubDefaultRefModel;
import nc.ui.er.util.BXUiUtil;
import nc.ui.erm.billpub.action.ContrastAction;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.erm.billpub.view.ErmBillBillFormHelper;
import nc.ui.erm.costshare.common.ErmForCShareUiUtil;
import nc.ui.erm.matterapp.common.MatterAppUiUtil;
import nc.ui.erm.util.ErUiUtil;
import nc.ui.erm.view.ERMOrgPane;
import nc.ui.erm.view.ErmCardPanelEventTransformer;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.arap.bx.util.BXParamConstant;
import nc.vo.bd.freecustom.FreeCustomVO;
import nc.vo.bd.fundplan.FundPlanVO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.er.util.StringUtils;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.resa.costcenter.CostCenterVO;
import nc.vo.uif2.LoginContext;

public class InitEventHandle implements BillEditListener2, BillEditListener,
		ValueChangedListener {
	private ErmBillBillForm editor = null;
	private ErmBillBillFormHelper helper = null;
	private EventHandleUtil eventUtil = null;
	private HeadAfterEditUtil headAfterEdit = null;
	private HeadFieldHandleUtil headFieldHandle = null;
	private BodyEventHandleUtil bodyEventHandleUtil = null;
	
	// 湖北广电 劳务费报销单 新增的表头编辑后事件
	private HeadAfterEditHandler headAfterEditHandler = null;
	
	
	
	public InitEventHandle(ErmBillBillForm editor) {
		this.editor = editor;
		this.helper = editor.getHelper();
		this.eventUtil = new EventHandleUtil(editor);
		this.headAfterEdit = new HeadAfterEditUtil(editor);
		this.headFieldHandle = new HeadFieldHandleUtil(editor);
		this.bodyEventHandleUtil = new BodyEventHandleUtil(editor);
		 
		// 给新增的表头编辑后事件赋值
		this.headAfterEditHandler = new HeadAfterEditHandler(editor);
	}

	public boolean beforeEdit(BillEditEvent e) {
		return true;
	}

	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		int pos = e.getPos();
		
		
		// 新增的表头编辑后事件
		this.headAfterEditHandler.doEdit(key);
		
		
		try {
			if (key.equals("djrq")) {
				afterEditBillDate(e);
			} else if (key.equals("pk_org_v")) {
				afterEditPk_org_v(e);
			} else if (key.equals(JKBXHeaderVO.PK_PCORG_V)) {
				afterEditOrg_v(e);
			} else if (key.equals("pk_pcorg")) {
				afterEditOrgField("pk_pcorg");
			} else if (key.equals(JKBXHeaderVO.FYDWBM_V)) {
				afterEditOrg_v(e);
			} else if (key.equals("fydwbm")) {
				afterEditOrgField("fydwbm");
			} else if (key.equals(JKBXHeaderVO.PK_PAYORG_V)) {
				afterEditOrg_v(e);
			} else if (key.equals(JKBXHeaderVO.PK_PAYORG)) {
				afterEditOrgField(JKBXHeaderVO.PK_PAYORG);
			} else if (key.equals(JKBXHeaderVO.DWBM_V)) {
				afterEditDwbm_v();
			} else if (key.equals("dwbm")) {
				afterEditOrgField("dwbm");
			} else if (key.equals(JKBXHeaderVO.FYDEPTID_V)) {
				afterEditFydeptid_v();
			} else if (key.equals(JKBXHeaderVO.DEPTID_V)) {
				afterEditDeptid_v();
			} else if (key.equals("fydeptid")) {
				String fydeptId = getHeadItemStrValue("fydeptid");
				String pk_org = getHeadItemStrValue("fydwbm");
				MultiVersionUtil.setHeadDeptMultiVersion(
						JKBXHeaderVO.FYDEPTID_V, pk_org, fydeptId,
						getBillCardPanel(), this.editor.isInit());
				afterEditFydeptid();
			} else if (key.equals("deptid")) {
				String fydeptId = getHeadItemStrValue("deptid");
				String pk_org = getHeadItemStrValue("dwbm");
				MultiVersionUtil.setHeadDeptMultiVersion(JKBXHeaderVO.DEPTID_V,
						pk_org, fydeptId, getBillCardPanel(),
						this.editor.isInit());
				afterEditDeptid();
			} else if (key.equals("hbbm")) {
				afterEditSupplier();
			} else if (key.equals("customer")) {
				afterEditCustomer();
			} else if (key.equals("paytarget")) {
				afterEditPayarget(true);
			} else if (key.equals("jkbxr")) {
				afterEditJkbxr(getHeadItemStrValue("jkbxr"));
			} else if ("freecust".equals(key)) {
				setHeadValue("custaccount", null);
			} else if (key.equals("receiver")) {
				this.headFieldHandle.editReceiver();
			} else if (key.equals("bzbm")) {
				afterEditBZBM(e);
			} else if ((key.equals("bbhl")) || (key.equals("globalbbhl"))
					|| (key.equals("groupbbhl"))) {
				String pk_org = getHeadItemStrValue("pk_org");
				String pk_currtype = getHeadItemStrValue("bzbm");

				boolean isEnabled = false;
				if (key.equals("bbhl"))
					isEnabled = MatterAppUiUtil.getOrgRateEnableStatus(pk_org,
							pk_currtype);
				else if (key.equals("groupbbhl"))
					isEnabled = MatterAppUiUtil.getGroupRateEnableStatus(
							pk_org, pk_currtype);
				else if (key.equals("globalbbhl")) {
					isEnabled = MatterAppUiUtil.getGlobalRateEnableStatus(
							pk_org, pk_currtype);
				}
				if (!isEnabled) {
					BillItem currinfoItem = this.editor.getBillCardPanel()
							.getHeadItem(key);
					currinfoItem.setValue(e.getOldValue());
				}
				afterHLChanged(e);
			} else if (key.equals("ybje")) {
				this.eventUtil.setHeadYfbByHead();
			} else if (key.equals("amount")) {
				try {
					this.editor.getHelper().calculateFinitemAndHeadTotal(
							this.editor);
					this.eventUtil.setHeadYFB();
				} catch (BusinessException e1) {
					nc.vo.er.exception.ExceptionHandler
							.handleExceptionRuntime(e1);
				}
			} else if (key.equals("total")) {
				this.eventUtil.setHeadYFB();
			} else if ("zy".equals(key)) {
				afterEditZy(e);
			} else if ("jobid".equals(key)) {
				getBillCardPanel().getHeadItem("projecttask").setValue(null);
				this.headFieldHandle.initProjTask();
			} else if (key.equals("iscostshare")) {
				this.eventUtil.afterEditIsCostShare();
			} else if (key.equals("isexpamt")) {
				this.eventUtil.afterEditIsExpamt();
			} else if (key.equals("cashproj")) {
				afterEditCashProj(e);
			}

			if (this.bodyEventHandleUtil.getUserdefine(0, e.getKey(), 2) != null) {
				String formula = this.bodyEventHandleUtil.getUserdefine(0,
						e.getKey(), 2);
				String[] strings = formula.split(";");
				for (String form : strings) {
					this.bodyEventHandleUtil.doFormulaAction(form, e.getKey(),
							-1, e.getTableCode(), e.getValue());
				}
			}

			if (((pos == 0) && ((key.equals("szxmid")) || (key.equals("jobid"))
					|| (key.equals("cashproj"))
					|| (key.equals(JKBXHeaderVO.PK_RESACOSTCENTER))
					|| (key.equals("pk_checkele")) || (key.equals("pk_pcorg"))
					|| (key.equals(JKBXHeaderVO.PK_PCORG_V))
					|| (key.equals("projecttask")) || (key
						.equals(JKBXHeaderVO.PK_RESACOSTCENTER))))
					|| (key.equals("pk_proline"))
					|| (key.equals("pk_brand"))
					|| (key.equals("jkbxr"))
					|| (key.equals("paytarget"))
					|| (key.equals("receiver"))
					|| (key.equals("skyhzh"))
					|| (key.equals("hbbm"))
					|| (key.equals("customer"))
					|| (key.equals("custaccount")) || (key.equals("freecust"))) {
				String pk_value = getHeadItemStrValue(key);
				this.helper.changeBusItemValue(key, pk_value);
			}

			if (!isImporting()) {
				this.editor.doReimRuleAction();
			}

			ErmForCShareUiUtil.afterEditHeadChangeCsharePageValue(
					this.editor.getBillCardPanel(), key);
		} catch (BusinessException e1) {
			nc.vo.er.exception.ExceptionHandler.handleExceptionRuntime(e1);
		}

		this.editor.getErmEventTransformer().afterEdit(e);
	}

	public void afterEditPayarget(boolean isEdit) throws BusinessException {
		Integer paytarget = (Integer) getHeadValue("paytarget");
		if (paytarget == null) {
			return;
		}

		if (paytarget.intValue() == 0) {
			getHeadItemUIRefPane("skyhzh").setEnabled(true);
			getHeadItemUIRefPane("custaccount").setEnabled(false);
		} else if (paytarget.intValue() == 1) {
			getHeadItemUIRefPane("skyhzh").setEnabled(false);
			getHeadItemUIRefPane("custaccount").setEnabled(true);
		} else if (paytarget.intValue() == 2) {
			getHeadItemUIRefPane("skyhzh").setEnabled(false);
			getHeadItemUIRefPane("custaccount").setEnabled(true);
		} else if (paytarget.intValue() == 3) {
			getHeadItemUIRefPane("skyhzh").setEnabled(false);
			getHeadItemUIRefPane("custaccount").setEnabled(false);
		}

		if (isEdit)
			setBillItemNull(new String[] { "skyhzh", "custaccount" },
					new String[] { "skyhzh", "custaccount",
							"freecust.bankaccount" });
	}

	private void setBillItemNull(String[] headFields, String[] bodyFields) {
		if (headFields != null) {
			for (String headField : headFields) {
				BillItem item = getBillCardPanel().getHeadItem(headField);
				if (item != null) {
					item.setValue(null);
				}
			}
		}

		if ((bodyFields != null) && (bodyFields.length > 0)) {
			String defaultMetaDataPath = "er_busitem";
			if (!isBxBill()) {
				defaultMetaDataPath = "jk_busitem";
			}

			BillTabVO[] billTabVOs = getBillCardPanel().getBillData()
					.getBillTabVOs(1);
			if ((billTabVOs != null) && (billTabVOs.length > 0))
				for (BillTabVO billTabVO : billTabVOs) {
					String metaDataPath = billTabVO.getMetadatapath();

					if ((metaDataPath == null)
							|| (defaultMetaDataPath.equals(metaDataPath))) {
						String tabcode = billTabVO.getTabcode();
						BillModel billModel = getBillCardPanel().getBillModel(
								tabcode);

						int rowCount = 0;
						if (billModel != null) {
							rowCount = billModel.getRowCount();
						}

						for (String bodyField : bodyFields) {
							BillItem bodyItem = getBillCardPanel().getBodyItem(
									tabcode, bodyField);
							if (bodyItem != null) {
								for (int i = 0; i < rowCount; i++) {
									getBillCardPanel().setBodyValueAt(null, i,
											bodyField, tabcode);
									billModel
											.execEditFormulaByKey(i, bodyField);
								}
							}
						}
						billModel.execLoadFormula();
					}
				}
		}
	}

	private void afterHLChanged(BillEditEvent e) throws BusinessException {
		if (!BXUiUtil.isExistBusiPage(this.editor.getBillCardPanel())) {
			UFDouble valueObject = (UFDouble) this.editor.getBillCardPanel()
					.getHeadItem("ybje").getValueObject();
			onlydealHeadBBje(this.editor.getBillCardPanel(), valueObject);
		} else {
			resetBodyFinYFB();

			this.eventUtil.setHeadBbje();
		}

		resetBodyCShare(e);
	}

	public static void onlydealHeadBBje(BillCardPanel billCardPanel, UFDouble yb) {
		UFDouble ybje = yb;

		UFDouble hl = null;
		UFDouble globalhl = null;
		UFDouble grouphl = null;

		Object bbhlValue = billCardPanel.getHeadItem("bbhl").getValueObject();
		if (bbhlValue != null) {
			hl = new UFDouble(bbhlValue.toString());
		}
		Object groupbbhl = billCardPanel.getHeadItem("groupbbhl")
				.getValueObject();
		if (groupbbhl != null) {
			grouphl = new UFDouble(groupbbhl.toString());
		}
		Object globalbbhl = billCardPanel.getHeadItem("globalbbhl")
				.getValueObject();
		if (globalbbhl != null) {
			globalhl = new UFDouble(globalbbhl.toString());
		}

		Object bzbmValue = billCardPanel.getHeadItem("bzbm").getValueObject();
		String pk_org = (String) billCardPanel.getHeadItem("pk_org")
				.getValueObject();

		String bzbm = null;
		if (bzbmValue == null) {
			return;
		}
		bzbm = bzbmValue.toString();
		try {
			int ybDecimalDigit = Currency.getCurrDigit(bzbm);
			int orgBbDecimalDigit = Currency.getCurrDigit(Currency
					.getOrgLocalCurrPK(pk_org));
			int groupByDecimalDigit = Currency.getCurrDigit(Currency
					.getGroupCurrpk(BXUiUtil.getPK_group()));
			int globalByDecimalDigit = Currency.getCurrDigit(Currency
					.getGlobalCurrPk(null));

			BXUiUtil.resetCardDecimalDigit(billCardPanel, ybDecimalDigit,
					JKBXHeaderVO.getYbjeField(), null);

			BXUiUtil.resetCardDecimalDigit(billCardPanel, orgBbDecimalDigit,
					JKBXHeaderVO.getOrgBbjeField(), null);

			BXUiUtil.resetCardDecimalDigit(billCardPanel, groupByDecimalDigit,
					JKBXHeaderVO.getHeadGroupBbjeField(), null);

			BXUiUtil.resetCardDecimalDigit(billCardPanel, globalByDecimalDigit,
					JKBXHeaderVO.getHeadGlobalBbjeField(), null);

			UFDouble[] bbje = Currency.computeYFB(pk_org, 1, bzbm, ybje, null,
					null, null, hl, BXUiUtil.getSysdate());

			billCardPanel.getHeadItem("bbje").setValue(bbje[2]);
			if (billCardPanel.getHeadItem("bbye") != null) {
				billCardPanel.getHeadItem("bbye").setValue(bbje[2]);
			}

			if ((billCardPanel.getHeadItem("cjkybje") != null)
					&& (billCardPanel.getHeadItem("cjkybje").getValueObject() != null)) {
				UFDouble cjkybje = (UFDouble) billCardPanel.getHeadItem(
						"cjkybje").getValueObject();
				bbje = Currency.computeYFB(pk_org, 1, bzbm, cjkybje, null,
						null, null, hl, BXUiUtil.getSysdate());
				if (billCardPanel.getHeadItem("cjkbbje") != null) {
					billCardPanel.getHeadItem("cjkbbje").setValue(bbje[2]);
				}
			}

			if ((billCardPanel.getHeadItem("hkybje") != null)
					&& (billCardPanel.getHeadItem("hkybje").getValueObject() != null)) {
				UFDouble hkybje = (UFDouble) billCardPanel
						.getHeadItem("hkybje").getValueObject();
				bbje = Currency.computeYFB(pk_org, 1, bzbm, hkybje, null, null,
						null, hl, BXUiUtil.getSysdate());
				if (billCardPanel.getHeadItem("hkbbje") != null) {
					billCardPanel.getHeadItem("hkbbje").setValue(bbje[2]);
				}
			}

			if ((billCardPanel.getHeadItem("zfbbje") != null)
					&& (billCardPanel.getHeadItem("zfbbje").getValueObject() != null)) {
				UFDouble zfybje = (UFDouble) billCardPanel
						.getHeadItem("zfybje").getValueObject();
				bbje = Currency.computeYFB(pk_org, 1, bzbm, zfybje, null, null,
						null, hl, BXUiUtil.getSysdate());
				if (billCardPanel.getHeadItem("zfbbje") != null) {
					billCardPanel.getHeadItem("zfbbje").setValue(bbje[2]);
				}

			}

			UFDouble[] je = Currency.computeYFB(pk_org, 1, bzbm, ybje, null,
					null, null, hl, BXUiUtil.getSysdate());
			UFDouble[] money = Currency.computeGroupGlobalAmount(je[0], je[2],
					bzbm, BXUiUtil.getSysdate(),
					billCardPanel.getHeadItem("pk_org").getValueObject()
							.toString(), billCardPanel.getHeadItem("pk_group")
							.getValueObject().toString(), globalhl, grouphl);

			if (billCardPanel.getHeadItem("groupbbje") != null) {
				billCardPanel.getHeadItem("groupbbje").setValue(money[0]);
			}
			if (billCardPanel.getHeadItem("groupbbye") != null) {
				billCardPanel.getHeadItem("groupbbye").setValue(money[0]);
			}
			if (billCardPanel.getHeadItem("globalbbje") != null) {
				billCardPanel.getHeadItem("globalbbje").setValue(money[1]);
			}
			if (billCardPanel.getHeadItem("globalbbye") != null) {
				billCardPanel.getHeadItem("globalbbye").setValue(money[1]);
			}

			if ((billCardPanel.getHeadItem("zfybje") != null)
					&& (billCardPanel.getHeadItem("zfybje").getValueObject() != null)) {
				UFDouble zfybje = (UFDouble) billCardPanel
						.getHeadItem("zfybje").getValueObject();
				je = Currency.computeYFB(pk_org, 1, bzbm, zfybje, null, null,
						null, hl, BXUiUtil.getSysdate());
				money = Currency.computeGroupGlobalAmount(je[0], je[2], bzbm,
						BXUiUtil.getSysdate(),
						billCardPanel.getHeadItem("pk_org").getValueObject()
								.toString(),
						billCardPanel.getHeadItem("pk_group").getValueObject()
								.toString(), globalhl, grouphl);

				if (billCardPanel.getHeadItem("groupzfbbje") != null) {
					billCardPanel.getHeadItem("groupzfbbje").setValue(money[0]);
				}
				if (billCardPanel.getHeadItem("globalzfbbje") != null) {
					billCardPanel.getHeadItem("globalzfbbje")
							.setValue(money[1]);
				}

			}

			if ((billCardPanel.getHeadItem("cjkybje") != null)
					&& (billCardPanel.getHeadItem("cjkybje").getValueObject() != null)) {
				UFDouble cjkybje = (UFDouble) billCardPanel.getHeadItem(
						"cjkybje").getValueObject();
				je = Currency.computeYFB(pk_org, 1, bzbm, cjkybje, null, null,
						null, hl, BXUiUtil.getSysdate());
				money = Currency.computeGroupGlobalAmount(je[0], je[2], bzbm,
						BXUiUtil.getSysdate(),
						billCardPanel.getHeadItem("pk_org").getValueObject()
								.toString(),
						billCardPanel.getHeadItem("pk_group").getValueObject()
								.toString(), globalhl, grouphl);

				if (billCardPanel.getHeadItem("groupcjkbbje") != null) {
					billCardPanel.getHeadItem("groupcjkbbje")
							.setValue(money[0]);
				}
				if (billCardPanel.getHeadItem("globalcjkbbje") != null) {
					billCardPanel.getHeadItem("globalcjkbbje").setValue(
							money[1]);
				}
			}

			if ((billCardPanel.getHeadItem("hkybje") != null)
					&& (billCardPanel.getHeadItem("hkybje").getValueObject() != null)) {
				UFDouble hkybje = (UFDouble) billCardPanel
						.getHeadItem("hkybje").getValueObject();
				je = Currency.computeYFB(pk_org, 1, bzbm, hkybje, null, null,
						null, hl, BXUiUtil.getSysdate());
				money = Currency.computeGroupGlobalAmount(je[0], je[2], bzbm,
						BXUiUtil.getSysdate(),
						billCardPanel.getHeadItem("pk_org").getValueObject()
								.toString(),
						billCardPanel.getHeadItem("pk_group").getValueObject()
								.toString(), globalhl, grouphl);

				if (billCardPanel.getHeadItem("grouphkbbje") != null) {
					billCardPanel.getHeadItem("grouphkbbje").setValue(money[0]);
				}
				if (billCardPanel.getHeadItem("globalhkbbje") != null)
					billCardPanel.getHeadItem("globalhkbbje")
							.setValue(money[1]);
			}
		} catch (BusinessException e) {
			nc.vo.er.exception.ExceptionHandler.handleExceptionRuntime(e);
		}
	}

	private void afterEditCustomer() {
		if (getBillCardPanel().getHeadItem("custaccount").isShowFlag()) {
			setDefaultCustaccountByCustomer();
		}

		this.headFieldHandle.initFreeCustByCustomer();
	}

	private void setDefaultCustaccountByCustomer() {
		Integer paytarget = (Integer) getHeadValue("paytarget");
		if ((paytarget != null) && (paytarget.equals(Integer.valueOf(2)))) {
			String cust = getHeadItemStrValue("customer");
			ICustomerPubService service = (ICustomerPubService) NCLocator
					.getInstance().lookup(ICustomerPubService.class.getName());
			try {
				String custaccount = service.getDefaultBankAcc(cust);
				setHeadValue("custaccount", custaccount);
				this.helper.changeBusItemValue("custaccount", custaccount);
			} catch (Exception ex) {
				setHeadValue("custaccount", null);
				nc.vo.er.exception.ExceptionHandler.consume(ex);
			}
		}
	}

	private void afterEditCashProj(BillEditEvent e) {
		UIRefPane refPane = getHeadItemUIRefPane(e.getKey());
		String pk_fundplan = refPane.getRefPK();
		try {
			if (!StringUtils.isEmpty(pk_fundplan)) {
				FundPlanVO fundPlanVO = ((IFundPlanQryService) NCLocator
						.getInstance().lookup(IFundPlanQryService.class))
						.queryFundPlanVOByPk(pk_fundplan);
				if (!StringUtils.isEmpty(fundPlanVO.getPk_cashflow()))
					setHeadValue("cashitem", fundPlanVO.getPk_cashflow());
			} else {
				String[] oldValue = (String[]) e.getOldValue();
				if (oldValue != null) {
					String oldV = oldValue[0];
					FundPlanVO fundPlanVO = ((IFundPlanQryService) NCLocator
							.getInstance().lookup(IFundPlanQryService.class))
							.queryFundPlanVOByPk(oldV);

					if ((!StringUtils.isEmpty(fundPlanVO.getPk_cashflow()))
							&& (fundPlanVO.getPk_cashflow()
									.equals(getHeadItemStrValue("cashitem")))) {
						setHeadValue("cashitem", null);
					}
				}
			}
		} catch (BusinessException e1) {
			nc.vo.er.exception.ExceptionHandler.handleExceptionRuntime(e1);
		}
	}

	private void setAccperiodMonth() {
		if (getBillCardPanel().getHeadItem("fydwbm").getValueObject() == null) {
			return;
		}

		String pk_org = getBillCardPanel().getHeadItem("fydwbm")
				.getValueObject().toString();
		BillItem expamtItem = getBillCardPanel().getHeadItem("isexpamt");
		if (expamtItem != null) {
			Object isExpamt = expamtItem.getValueObject();
			if ((isExpamt != null) && (isExpamt.toString().equals("true")))
				try {
					AccperiodmonthVO accperiodmonthVO = ErAccperiodUtil
							.getAccperiodmonthByUFDate(
									pk_org,
									(UFDate) getBillCardPanel().getHeadItem(
											"djrq").getValueObject());

					((AccPeriodDefaultRefModel) ((UIRefPane) getBillCardPanel()
							.getHeadItem("start_period").getComponent())
							.getRefModel())
							.setDefaultpk_accperiodscheme(accperiodmonthVO
									.getPk_accperiodscheme());

					getBillCardPanel().getHeadItem("start_period").setValue(
							accperiodmonthVO.getPk_accperiodmonth());
				} catch (InvalidAccperiodExcetion ex) {
					nc.vo.er.exception.ExceptionHandler
							.handleExceptionRuntime(ex);
				}
		}
	}

	private void afterEditBillDate(BillEditEvent e) throws BusinessException {
		if (getBillCardPanel().getHeadItem("zhrq") != null) {
			Object billDate = getBillCardPanel().getHeadItem("djrq")
					.getValueObject();

			int days = SysInit.getParaInt(getPk_org(),
					BXParamConstant.PARAM_ER_RETURN_DAYS).intValue();
			if ((billDate != null) && (billDate.toString().length() > 0)) {
				UFDate zhrq = ((UFDate) billDate).getDateAfter(days);
				setHeadValue("zhrq", zhrq);
			}
		}

		String pk_org = getHeadItemStrValue("pk_org");
		if (!StringUtil.isEmpty(pk_org)) {
			this.helper.setHeadOrgMultiVersion(new String[] { "pk_org_v",
					JKBXHeaderVO.FYDWBM_V, JKBXHeaderVO.DWBM_V,
					JKBXHeaderVO.PK_PCORG_V }, new String[] { "pk_org",
					"fydwbm", "dwbm", "pk_pcorg" });
		}

		String pk_dept = getHeadItemStrValue("deptid");
		if (!StringUtil.isEmpty(pk_dept)) {
			this.helper.setHeadDeptMultiVersion(
					new String[] { JKBXHeaderVO.DEPTID_V },
					getHeadItemStrValue("dwbm"), pk_dept);
		}
		String pk_fydept = getHeadItemStrValue("fydeptid");
		if (!StringUtil.isEmpty(pk_fydept)) {
			this.helper.setHeadDeptMultiVersion(
					new String[] { JKBXHeaderVO.FYDEPTID_V },
					getHeadItemStrValue("fydwbm"), pk_fydept);
		}

		resetHlAndJe(e);
	}

	private void resetHlAndJe(BillEditEvent e) throws BusinessException {
		String pk_currtype = getHeadItemStrValue("bzbm");
		if (pk_currtype != null) {
			String pk_org = getHeadItemStrValue("pk_org");

			UFDate date = (UFDate) getBillCardPanel().getHeadItem("djrq")
					.getValueObject();
			try {
				this.helper.setCurrencyInfo(pk_org,
						Currency.getOrgLocalCurrPK(pk_org), pk_currtype, date);
			} catch (BusinessException e1) {
				nc.vo.er.exception.ExceptionHandler.handleException(e1);
			}
		}

		afterHLChanged(e);
	}

	private void afterEditOrg_v(BillEditEvent evt) throws BusinessException {
		String pk_org_v = getHeadItemStrValue(evt.getKey());
		afterEditMultiVersionOrgField(evt.getKey(), pk_org_v,
				JKBXHeaderVO.getOrgFieldByVField(evt.getKey()));
	}

	private void afterEditPk_org_v(BillEditEvent e) throws BusinessException {
		String newpk_org_v = null;
		Object value = e.getValue();
		if ((value instanceof String[]))
			newpk_org_v = ((String[]) (String[]) value)[0];
		else {
			newpk_org_v = (String) value;
		}
		afterEditMultiVersionOrgField(e.getKey(), newpk_org_v,
				JKBXHeaderVO.getOrgFieldByVField(e.getKey()));
	}

	private void setZeroForBodyBbjeField(String[] bodyFields) {
		String[] bodytablecodes = getBillCardPanel().getBillData()
				.getBodyTableCodes();
		for (String bodytablecode : bodytablecodes)
			for (String bodyField : bodyFields) {
				int rowCount = getBillCardPanel().getBillModel(bodytablecode)
						.getRowCount();
				for (int i = 0; i < rowCount; i++)
					getBillCardPanel().setBodyValueAt(UFDouble.ZERO_DBL, i,
							bodyField, bodytablecode);
			}
	}

	private void afterEditDwbm_v() throws BusinessException {
		String pk_org_v = getHeadItemStrValue(JKBXHeaderVO.DWBM_V);
		String oid = this.eventUtil.getBillHeadFinanceOrg(JKBXHeaderVO.DWBM_V,
				pk_org_v, getBillCardPanel());
		setHeadValue("dwbm", oid);

		afterEditDwbm();
	}

	public void afterEditSupplier() {
		setHeadValue("freecust", null);

		if (getBillCardPanel().getHeadItem("custaccount").isShowFlag()) {
			setDefaultCustaccountBySupplier();
		}
		this.headFieldHandle.initFreeCustBySupplier();
	}

	private void setDefaultCustaccountBySupplier() {
		Integer paytarget = (Integer) getHeadValue("paytarget");
		if ((paytarget != null) && (paytarget.equals(Integer.valueOf(1)))) {
			String supplier = getHeadItemStrValue("hbbm");
			ISupplierPubService service = (ISupplierPubService) NCLocator
					.getInstance().lookup(ISupplierPubService.class.getName());
			try {
				String custaccount = service.getDefaultBankAcc(supplier);
				setHeadValue("custaccount", custaccount);
				this.helper.changeBusItemValue("custaccount", custaccount);
			} catch (Exception ex) {
				setHeadValue("custaccount", null);
				nc.vo.er.exception.ExceptionHandler.consume(ex);
			}
		}
	}

	private void afterEditDeptid_v() throws BusinessException {
		String pk_dept_v = getHeadItemStrValue(JKBXHeaderVO.DEPTID_V);
		String pk_dept = this.eventUtil.getBillHeadDept(JKBXHeaderVO.DEPTID_V,
				pk_dept_v);
		setHeadValue("deptid", pk_dept);
		afterEditDeptid();
	}

	private void afterEditFydeptid_v() throws BusinessException {
		String pk_fydept_v = getHeadItemStrValue(JKBXHeaderVO.FYDEPTID_V);
		String pk_fydept = this.eventUtil.getBillHeadDept(
				JKBXHeaderVO.FYDEPTID_V, pk_fydept_v);
		setHeadValue("fydeptid", pk_fydept);
		afterEditFydeptid();
	}

	private void afterEditDeptid() throws BusinessException {
		setHeadValue("jkbxr", null);
		String deptid = getHeadItemStrValue("deptid");
		this.helper.changeBusItemValue("deptid", deptid);
	}

	private void afterEditFydeptid() throws BusinessException {
		String pk_fydept = getHeadItemStrValue("fydeptid");

		setCostCenter(pk_fydept);
	}

	public void setCostCenter(String pk_fydept) throws BusinessException {
		if (StringUtil.isEmpty(pk_fydept)) {
			setHeadValue(JKBXHeaderVO.PK_RESACOSTCENTER, null);
			setHeadValue("pk_checkele", null);
			setHeadValue("pk_pcorg", null);
			setHeadValue(JKBXHeaderVO.PK_PCORG_V, null);
			return;
		}
		try {
			String pk_costcenter = null;
			String pk_pcorg = null;
			String pk_pcorg_v = null;

			CostCenterVO[] vos = ((ICostCenterQueryOpt) NCLocator.getInstance()
					.lookup(ICostCenterQueryOpt.class))
					.queryCostCenterVOByDept(new String[] { pk_fydept });
			if (vos != null) {
				CostCenterVO[] arr$ = vos;
				int len$ = arr$.length;
				int i$ = 0;
				if (i$ < len$) {
					CostCenterVO vo = arr$[i$];
					pk_costcenter = vo.getPk_costcenter();
					pk_pcorg = vo.getPk_profitcenter();
				}
			}

			if (pk_pcorg != null) {
				setHeadValue("pk_pcorg", pk_pcorg);
				MultiVersionUtil.setHeadOrgMultiVersion(
						JKBXHeaderVO.PK_PCORG_V, (String) getBillCardPanel()
								.getHeadItem("pk_pcorg").getValueObject(),
						getBillCardPanel(), this.editor);

				pk_pcorg_v = getHeadItemStrValue(JKBXHeaderVO.PK_PCORG_V);
				setHeadValue(JKBXHeaderVO.PK_RESACOSTCENTER, pk_costcenter);
				setHeadValue("pk_checkele", null);
			} else {
				setHeadValue("pk_pcorg", null);
				setHeadValue(JKBXHeaderVO.PK_PCORG_V, null);
				setHeadValue(JKBXHeaderVO.PK_RESACOSTCENTER, null);
				setHeadValue("pk_checkele", null);
			}

			this.helper.changeBusItemValue("pk_pcorg", pk_pcorg);
			this.helper.changeBusItemValue("pk_pcorg_v", pk_pcorg_v);
			this.helper.changeBusItemValue("pk_resacostcenter", pk_costcenter);
			this.helper.changeBusItemValue("pk_checkele", null);
		} catch (BusinessException e) {
			nc.vo.fipub.exception.ExceptionHandler.handleException(e);
			return;
		}
	}

	private void afterEditZy(BillEditEvent evt) throws BusinessException {
		UIRefPane refPane = (UIRefPane) getBillCardPanel().getHeadItem("zy")
				.getComponent();
		String text = refPane.getText();
		refPane.getRefModel().matchPkData(text);
		refPane.getUITextField().setToolTipText(text);
	}

	private void afterEditMultiVersionOrgField(String orgVField,
			String orgVValue, String orgField) throws BusinessException {
		String newOrgValue = this.eventUtil.getBillHeadFinanceOrg(orgVField,
				orgVValue, getBillCardPanel());

		setHeadValue(orgField, newOrgValue);

		afterEditOrgField(orgField);
	}

	private void afterEditOrgField(String orgField) throws BusinessException {
		if ("pk_org".equals(orgField)) {
			afterEditPk_org();
			this.headFieldHandle.initCashProj();
			this.headFieldHandle.filtFkyhzh();
		} else if ("fydwbm".equals(orgField)) {
			afterEditFydwbm();
		} else if ("dwbm".equals(orgField)) {
			afterEditDwbm();
		} else if ("pk_pcorg".equals(orgField)) {
			MultiVersionUtil.setHeadOrgMultiVersion(JKBXHeaderVO.PK_PCORG_V,
					(String) getBillCardPanel().getHeadItem("pk_pcorg")
							.getValueObject(), getBillCardPanel(), this.editor);

			getBillCardPanel().getHeadItem("pk_checkele").setValue(null);
			getBillCardPanel().getHeadItem(JKBXHeaderVO.PK_RESACOSTCENTER)
					.setValue(null);
			this.headFieldHandle.initPk_Checkele();
			this.headFieldHandle.initResaCostCenter();
		} else if (JKBXHeaderVO.PK_PAYORG.equals(orgField)) {
			MultiVersionUtil.setHeadOrgMultiVersion(
					JKBXHeaderVO.PK_PAYORG_V,
					(String) getBillCardPanel().getHeadItem(
							JKBXHeaderVO.PK_PAYORG).getValueObject(),
					getBillCardPanel(), this.editor);

			this.headAfterEdit.initPayorgentityItems(true);
			this.headFieldHandle.initCashProj();
			this.headFieldHandle.initFkyhzh();
		}
	}

	private void afterEditFydwbm() throws BusinessException {
		MultiVersionUtil.setHeadOrgMultiVersion(JKBXHeaderVO.FYDWBM_V,
				(String) getBillCardPanel().getHeadItem("fydwbm")
						.getValueObject(), getBillCardPanel(), this.editor);

		this.headAfterEdit.initCostentityItems(true);
		this.headFieldHandle.initProj();
		this.headFieldHandle.initProjTask();
		this.headFieldHandle.initSzxm();
		setAccperiodMonth();

		setHeadValue("custaccount", null);

		this.helper.changeBusItemValue("customer", null);
		this.helper.changeBusItemValue("hbbm", null);
		this.helper.changeBusItemValue("custaccount", null);
		this.helper.changeBusItemValue("freecust", null);
		this.helper.changeBusItemValue("freecust.bankaccount", null);
	}

	private void afterEditJkbxr(String jkbxr) throws BusinessException {
		if (StringUtil.isEmpty(jkbxr)) {
			return;
		}

		AbstractRefTreeModel refModel = (AbstractRefTreeModel) getHeadItemUIRefPane(
				"jkbxr").getRefModel();
		String pk_dept = refModel.getClassJoinValue();
		if (pk_dept != null) {
			Object pk_deptid = this.editor.getBillCardPanel()
					.getHeadItem("deptid").getValueObject();
			if ((pk_deptid == null) || (!pk_deptid.equals(pk_dept))) {
				this.eventUtil.setHeadNotNullValue("deptid", pk_dept);
				this.helper.changeBusItemValue("deptid", pk_dept);
			}
		} else {
			PsnjobVO[] jobs = (PsnjobVO[]) CacheUtil.getVOArrayByPkArray(
					PsnjobVO.class, "PK_PSNDOC", new String[] { jkbxr });

			if (jobs == null) {
				IBxUIControl pd = (IBxUIControl) NCLocator.getInstance()
						.lookup(IBxUIControl.class);
				jobs = pd.queryPsnjobVOByPsnPK(jkbxr);
			}

			if ((jobs != null) && (jobs.length > 1)) {
				String mainOrg = null;
				String mainDept = null;
				List orgList = new ArrayList();
				List deptList = new ArrayList();

				for (PsnjobVO vo : jobs) {
					orgList.add(vo.getPk_org());
					deptList.add(vo.getPk_dept());
					if (vo.getIsmainjob().equals(UFBoolean.TRUE)) {
						mainOrg = vo.getPk_org();
						mainDept = vo.getPk_dept();
					}
				}

				Object pk_org = this.editor.getBillCardPanel()
						.getHeadItem("dwbm").getValueObject();
				if ((pk_org == null) || (!orgList.contains(pk_org.toString()))) {
					this.eventUtil.setHeadNotNullValue("dwbm", mainOrg);
					this.helper.changeBusItemValue("dwbm", mainOrg);
				}

				Object pk_deptid = this.editor.getBillCardPanel()
						.getHeadItem("deptid").getValueObject();
				if ((pk_deptid == null)
						|| (!deptList.contains(pk_deptid.toString()))) {
					this.eventUtil.setHeadNotNullValue("deptid", mainDept);
					this.helper.changeBusItemValue("deptid", mainDept);
				}
			} else {
				String[] values = ErUiUtil.getPsnDocInfoById(jkbxr);
				if ((values != null) && (values.length > 0)
						&& (!StringUtil.isEmpty(values[1]))
						&& (!StringUtil.isEmpty(values[2]))) {
					this.eventUtil.setHeadNotNullValue("dwbm", values[2]);
					this.helper.changeBusItemValue("dwbm", values[2]);

					this.eventUtil.setHeadNotNullValue("deptid", values[1]);
					this.helper.changeBusItemValue("deptid", values[1]);
				}
			}

		}

		Object pk_org = this.editor.getBillCardPanel().getHeadItem("dwbm")
				.getValueObject();
		if (pk_org != null) {
			this.editor.getHelper().setHeadOrgMultiVersion(
					new String[] { JKBXHeaderVO.DWBM_V },
					new String[] { "dwbm" });

			this.editor.getHelper().setHeadDeptMultiVersion(
					JKBXHeaderVO.DEPTID_V, pk_org.toString(), "deptid");
		}

		editSkInfo();

		clearContrast();
	}

	private void afterEditFreecust(BillEditEvent e) {
		UIRefPane refPane = getHeadItemUIRefPane(e.getKey());
		String pk = refPane.getRefPK();

		String vBankaccount = BXUiUtil.getColValue(
				new FreeCustomVO().getTableName(), "bankaccount",
				"pk_freecustom", pk);
		if ((vBankaccount != null) && (vBankaccount.length() > 0)
				&& (getHeadItemStrValue("custaccount") == null))
			setHeadValue("custaccount", vBankaccount);
	}

	private void editSkInfo() throws BusinessException {
		if (!isPersonPaytarget()) {
			return;
		}

		String jkbxr = getHeadItemStrValue("jkbxr");

		if (StringUtil.isEmpty(jkbxr)) {
			return;
		}

		boolean isChangeReceiver = false;

		if ("20110RB".equals(this.editor.getModel().getContext().getNodeCode())) {
			isChangeReceiver = true;
		} else {
			String receiver = getHeadItemStrValue("receiver");
			BillItem receiverItem = this.editor.getBillCardPanel().getHeadItem(
					"receiver");
			boolean baseTableCodeShow = receiverItem.isBaseTableCodeShow();
			if ((jkbxr != null) && (!jkbxr.equals(receiver))
					&& (baseTableCodeShow)) {
				if (receiver != null) {
					if (MessageDialog.showYesNoDlg(
							this.editor,
							NCLangRes4VoTransl.getNCLangRes().getStrByID(
									"expensepub_0", "02011002-0019"),
							NCLangRes4VoTransl.getNCLangRes().getStrByID(
									"expensepub_0", "02011002-0021")) == 4) {
						isChangeReceiver = true;
					}
				} else
					isChangeReceiver = true;
			} else if (!baseTableCodeShow) {
				isChangeReceiver = true;
			}
		}

		if (isChangeReceiver) {
			setHeadValue("receiver", jkbxr);
			this.helper.changeBusItemValue("receiver", jkbxr);
			this.headFieldHandle.editReceiver();
		}
	}

	private void afterEditBZBM(BillEditEvent e) throws BusinessException {
		int rowCount = getBillCardPanel().getBillModel().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			getBillCardPanel().setBodyValueAt(null, i, "skyhzh_ID");
			getBillCardPanel().setBodyValueAt(null, i, "custaccount_ID");
		}
		getBillCardPanel().setHeadItem("skyhzh", null);
		getBillCardPanel().setHeadItem("custaccount", null);
		getBillCardPanel().setHeadItem("fkyhzh", null);
		getBillCardPanel().setHeadItem(JKBXHeaderVO.PK_CASHACCOUNT, null);
		this.headFieldHandle.filtFkyhzh();
		this.headFieldHandle.initCashAccount();

		String pk_currtype = getHeadItemStrValue("bzbm");
		if (pk_currtype != null) {
			String pk_org = getHeadItemStrValue("pk_org");

			UFDate date = (UFDate) getBillCardPanel().getHeadItem("djrq")
					.getValueObject();

			this.helper.setCurrencyInfo(pk_org,
					Currency.getOrgLocalCurrPK(pk_org), pk_currtype, date);
		}

		clearContrast();

		afterHLChanged(e);

		clearVerifyPage();
	}

	private void clearVerifyPage() {
		this.editor.setVerifyAccrued(false);
		BillModel billModel = this.editor.getBillCardPanel().getBillModel(
				"accrued_verify");
		if (billModel != null)
			billModel.clearBodyData();
	}

	public void resetBodyCShare(BillEditEvent e) {
		BillModel csbillModel = getBillCardPanel().getBillModel(
				"er_cshare_detail");
		if ((csbillModel != null) && (csbillModel.getRowCount() != 0)) {
			csbillModel.setNeedCalculate(false);
			for (int rowNum = 0; rowNum < csbillModel.getRowCount(); rowNum++) {
				if ((e != null)
						&& ((e.getKey().equals("bbhl"))
								|| (e.getKey().equals("groupbbhl")) || (e
									.getKey().equals("globalbbhl")))) {
					ErmForCShareUiUtil.setRateAndAmountNEW(rowNum,
							getBillCardPanel(), e.getKey());
				} else
					ErmForCShareUiUtil.setRateAndAmount(rowNum,
							getBillCardPanel());

			}

			csbillModel.setNeedCalculate(true);
			try {
				ErmForCShareUiUtil.calculateHeadTotal(getBillCardPanel());
			} catch (BusinessException e1) {
				nc.vo.er.exception.ExceptionHandler.handleExceptionRuntime(e1);
			}
		}
	}

	private void filterFkyhzh(String pk_currtype) {
		UIRefPane refPane = (UIRefPane) getBillCardPanel()
				.getHeadItem("fkyhzh").getComponent();
		BankaccSubDefaultRefModel model = (BankaccSubDefaultRefModel) refPane
				.getRefModel();
		String prefix = "pk_currtype=";
		String pk_org = (String) getBillCardPanel().getHeadItem("pk_org")
				.getValueObject();
		if (StringUtil.isEmpty(pk_currtype)) {
			model.setWherePart(null);
			model.setPk_org(pk_org);
			return;
		}
		model.setPk_org(pk_org);
		model.setWherePart("pk_currtype='" + pk_currtype + "'");

		List pkValueList = new ArrayList();
		Vector vct = model.reloadData();
		Iterator it = vct.iterator();
		int index = model.getFieldIndex("bd_bankaccsub.pk_bankaccsub");
		while (it.hasNext()) {
			Vector next = (Vector) it.next();
			pkValueList.add((String) next.get(index));
		}
		String refPK = refPane.getRefPK();
		if (!pkValueList.contains(refPK))
			refPane.setPK(null);
	}

	public void resetBodyFinYFB() {
		String[] billTableVos = getBillCardPanel().getBillData().getTableCodes(
				1);
		for (String tableCode : billTableVos)
			if (!"er_cshare_detail".equals(tableCode)) {
				BillModel billModel = getBillCardPanel()
						.getBillModel(tableCode);
				if ((billModel != null) && (billModel.getBodyItems() != null)) {
					BXBusItemVO[] bf = (BXBusItemVO[]) billModel
							.getBodyValueVOs(BXBusItemVO.class.getName());
					int length = bf.length;

					String bzbm = null;
					if (getHeadValue("bzbm") != null) {
						bzbm = getHeadValue("bzbm").toString();
					}
					if (getHeadValue("pk_org") != null) {
						billModel.setNeedCalculate(false);

						Map jebbCache = new HashMap();
						for (int i = 0; i < length; i++) {
							transFinYbjeToBbje(i, bzbm, tableCode, jebbCache);
						}
						billModel.setNeedCalculate(true);
					}
				}
			}
	}

	protected void transFinYbjeToBbje(int row, String bzbm, String currPage,
			Map<UFDouble, UFDouble[]> jebbCache) {
		BillCardPanel panel = getBillCardPanel();
		UFDouble ybje = (UFDouble) panel.getBillModel(currPage).getValueAt(row,
				"ybje");
		UFDouble cjkybje = (UFDouble) panel.getBillModel(currPage).getValueAt(
				row, "cjkybje");
		UFDouble hkybje = (UFDouble) panel.getBillModel(currPage).getValueAt(
				row, "hkybje");
		UFDouble zfybje = (UFDouble) panel.getBillModel(currPage).getValueAt(
				row, "zfybje");
		ybje = ybje == null ? UFDouble.ZERO_DBL : ybje;
		cjkybje = cjkybje == null ? UFDouble.ZERO_DBL : cjkybje;
		hkybje = hkybje == null ? UFDouble.ZERO_DBL : hkybje;
		zfybje = zfybje == null ? UFDouble.ZERO_DBL : zfybje;

		UFDouble hl = null;
		UFDouble globalhl = null;
		UFDouble grouphl = null;
		if (getBillCardPanel().getHeadItem("bbhl").getValueObject() != null) {
			hl = (UFDouble) getBillCardPanel().getHeadItem("bbhl")
					.getValueObject();
		}
		if (getBillCardPanel().getHeadItem("groupbbhl").getValueObject() != null) {
			grouphl = (UFDouble) getBillCardPanel().getHeadItem("groupbbhl")
					.getValueObject();
		}
		if (getBillCardPanel().getHeadItem("globalbbhl").getValueObject() != null) {
			globalhl = (UFDouble) getBillCardPanel().getHeadItem("globalbbhl")
					.getValueObject();
		}
		try {
			if (jebbCache.get(UFDouble.ZERO_DBL) == null) {
				UFDouble[] bbje = { UFDouble.ZERO_DBL, UFDouble.ZERO_DBL,
						UFDouble.ZERO_DBL };
				jebbCache.put(UFDouble.ZERO_DBL, bbje);
			}

			if (jebbCache.get(ybje) == null) {
				UFDouble[] bbje = Currency.computeYFB(getPk_org(), 1, bzbm,
						ybje, null, null, null, hl, BXUiUtil.getSysdate());
				UFDouble[] ybggBbje = Currency
						.computeGroupGlobalAmount(bbje[0], bbje[2], bzbm,
								BXUiUtil.getSysdate(), getBillCardPanel()
										.getHeadItem("pk_org").getValueObject()
										.toString(), getBillCardPanel()
										.getHeadItem("pk_group")
										.getValueObject().toString(), globalhl,
								grouphl);

				UFDouble[] bbjes = { bbje[2], ybggBbje[0], ybggBbje[1] };
				jebbCache.put(ybje, bbjes);
			}

			UFDouble[] ybBbjes = (UFDouble[]) jebbCache.get(ybje);
			panel.getBillModel(currPage).setValueAt(ybBbjes[0], row, "bbje");
			panel.getBillModel(currPage).setValueAt(ybBbjes[0], row, "bbye");
			panel.getBillModel(currPage).setValueAt(ybBbjes[1], row,
					"groupbbje");
			panel.getBillModel(currPage).setValueAt(ybBbjes[2], row,
					"globalbbje");
			panel.getBillModel(currPage).setValueAt(ybBbjes[1], row,
					"groupbbye");
			panel.getBillModel(currPage).setValueAt(ybBbjes[2], row,
					"globalbbye");

			if (jebbCache.get(zfybje) == null) {
				UFDouble[] zfbbje = Currency.computeYFB(getPk_org(), 1, bzbm,
						zfybje, null, null, null, hl, BXUiUtil.getSysdate());

				UFDouble[] zfggBbje = Currency
						.computeGroupGlobalAmount(zfbbje[0], zfbbje[2], bzbm,
								BXUiUtil.getSysdate(), getBillCardPanel()
										.getHeadItem("pk_org").getValueObject()
										.toString(), getBillCardPanel()
										.getHeadItem("pk_group")
										.getValueObject().toString(), globalhl,
								grouphl);

				UFDouble[] bbjes = { zfbbje[2], zfggBbje[0], zfggBbje[1] };
				jebbCache.put(zfybje, bbjes);
			}
			UFDouble[] zfBbjes = (UFDouble[]) jebbCache.get(zfybje);
			panel.getBillModel(currPage).setValueAt(zfBbjes[0], row, "zfbbje");
			panel.getBillModel(currPage).setValueAt(zfBbjes[1], row,
					"groupzfbbje");
			panel.getBillModel(currPage).setValueAt(zfBbjes[2], row,
					"globalzfbbje");

			if (jebbCache.get(cjkybje) == null) {
				UFDouble[] cjkbbje = Currency.computeYFB(getPk_org(), 1, bzbm,
						cjkybje, null, null, null, hl, BXUiUtil.getSysdate());

				UFDouble[] cjkggBbje = Currency
						.computeGroupGlobalAmount(cjkbbje[0], cjkbbje[2], bzbm,
								BXUiUtil.getSysdate(), getBillCardPanel()
										.getHeadItem("pk_org").getValueObject()
										.toString(), getBillCardPanel()
										.getHeadItem("pk_group")
										.getValueObject().toString(), globalhl,
								grouphl);

				UFDouble[] bbjes = { cjkbbje[2], cjkggBbje[0], cjkggBbje[1] };
				jebbCache.put(cjkybje, bbjes);
			}

			UFDouble[] cjkBbjes = (UFDouble[]) jebbCache.get(cjkybje);
			panel.getBillModel(currPage)
					.setValueAt(cjkBbjes[0], row, "cjkbbje");
			panel.getBillModel(currPage).setValueAt(cjkBbjes[1], row,
					"groupcjkbbje");
			panel.getBillModel(currPage).setValueAt(cjkBbjes[2], row,
					"globalcjkbbje");

			if (jebbCache.get(hkybje) == null) {
				UFDouble[] hkbbje = Currency.computeYFB(getPk_org(), 1, bzbm,
						hkybje, null, null, null, hl, BXUiUtil.getSysdate());

				UFDouble[] hkggBbje = Currency
						.computeGroupGlobalAmount(hkbbje[0], hkbbje[2], bzbm,
								BXUiUtil.getSysdate(), getBillCardPanel()
										.getHeadItem("pk_org").getValueObject()
										.toString(), getBillCardPanel()
										.getHeadItem("pk_group")
										.getValueObject().toString(), globalhl,
								grouphl);

				UFDouble[] bbjes = { hkbbje[2], hkggBbje[0], hkggBbje[1] };
				jebbCache.put(hkybje, bbjes);
			}

			UFDouble[] hjBbjes = (UFDouble[]) jebbCache.get(hkybje);
			panel.getBillModel(currPage).setValueAt(hjBbjes[0], row, "hkbbje");
			panel.getBillModel(currPage).setValueAt(hjBbjes[1], row,
					"grouphkbbje");
			panel.getBillModel(currPage).setValueAt(hjBbjes[2], row,
					"globalhkbbje");

			int rowState = panel.getBillModel(currPage).getRowState(row);
			if (1 != rowState)
				panel.getBillModel(currPage).setRowState(row, 2);
		} catch (BusinessException e) {
			nc.vo.er.exception.ExceptionHandler.consume(e);
		}
	}

	private void clearContrast() throws BusinessException, ValidationException {
		ContrastAction.doContrastToUI(this.editor.getBillCardPanel(),
				this.editor.getJKBXVO(), new ArrayList(), this.editor);

		this.editor.setContrast(true);
	}

	private void afterEditDwbm() throws BusinessException {
		MultiVersionUtil.setHeadOrgMultiVersion(JKBXHeaderVO.DWBM_V,
				(String) getBillCardPanel().getHeadItem("dwbm")
						.getValueObject(), getBillCardPanel(), this.editor);

		this.headAfterEdit.initUseEntityItems(true);
		String dwbm = getHeadItemStrValue("dwbm");
		this.helper.changeBusItemValue("dwbm", dwbm);

		String dept = getHeadItemStrValue("deptid");
		this.helper.changeBusItemValue("dwbm", dept);

		String jkbxr = getHeadItemStrValue("jkbxr");
		this.helper.changeBusItemValue("jkbxr", jkbxr);

		String receiver = getHeadItemStrValue("receiver");
		this.helper.changeBusItemValue("receiver", receiver);
	}

	private void afterEditPk_org() throws BusinessException {
		String pk_org = getHeadItemStrValue("pk_org");
		this.helper.setpk_org2Card(pk_org);

		if ((getBillCardPanel().getHeadItem("fydwbm") != null)
				&& (getBillCardPanel().getHeadItem(JKBXHeaderVO.FYDWBM_V)
						.getValueObject() == null)) {
			setHeadValue("fydwbm", pk_org);
			afterEditOrgField("fydwbm");
		}

		if ((getBillCardPanel().getHeadItem(JKBXHeaderVO.DWBM_V) != null)
				&& (getBillCardPanel().getHeadItem(JKBXHeaderVO.DWBM_V)
						.getValueObject() == null)) {
			setHeadValue("dwbm", pk_org);
			afterEditDwbm();
		}

		List keyList = this.editor.getPanelEditableKeyList();
		if (keyList != null) {
			for (int i = 0; i < keyList.size(); i++) {
				getBillCardPanel().getHeadItem((String) keyList.get(i))
						.setEnabled(true);
			}

			this.editor.setPanelEditableKeyList(null);
		}

		if (!"20110CBSG".equals(getNodeCode())) {
			if (pk_org != null) {
				this.helper.setDefaultWithOrg();

				this.helper.setHeadOrgMultiVersion(new String[] { "pk_org_v",
						JKBXHeaderVO.FYDWBM_V, JKBXHeaderVO.DWBM_V,
						JKBXHeaderVO.PK_PCORG_V, JKBXHeaderVO.PK_PAYORG_V },
						new String[] { "pk_org", "fydwbm", "dwbm", "pk_pcorg",
								JKBXHeaderVO.PK_PAYORG });

				this.helper.setHeadDeptMultiVersion(JKBXHeaderVO.DEPTID_V,
						getHeadItemStrValue("dwbm"), "deptid");
				this.helper.setHeadDeptMultiVersion(JKBXHeaderVO.FYDEPTID_V,
						getHeadItemStrValue("fydwbm"), "fydeptid");
			}
			this.headAfterEdit.initPayentityItems(true);
		}

		if ("20110BO".equals(getNodeCode())) {
			this.helper.checkQCClose(pk_org);
		}

		if (getHeadValue("pk_org") != null) {
			String pk_loccurrency = Currency
					.getOrgLocalCurrPK((String) getHeadValue("pk_org"));
			if ((pk_loccurrency != null) && (getHeadValue("bzbm") != null)
					&& (!pk_loccurrency.equals(getHeadValue("bzbm")))) {
				setHeadValue("bzbm", pk_loccurrency);

				afterEditBZBM(new BillEditEvent(getBillCardPanel().getHeadItem(
						"bzbm"), pk_loccurrency, "bzbm"));
			}
			afterHLChanged(null);
		} else {
			for (String headBbjeField : JKBXHeaderVO.getBbjeField()) {
				setHeadValue(headBbjeField, UFDouble.ZERO_DBL);
			}
			setZeroForBodyBbjeField(BXBusItemVO.getBodyOrgBbjeField());
			setZeroForBodyBbjeField(BXBusItemVO.getBodyGroupBbjeField());
			setZeroForBodyBbjeField(BXBusItemVO.getBodyGlobalBbjeField());
		}
	}

	protected void setHeadValue(String key, Object value) {
		if (getBillCardPanel().getHeadItem(key) != null)
			getBillCardPanel().getHeadItem(key).setValue(value);
	}

	protected void setHeadValues(String[] key, Object[] value) {
		for (int i = 0; i < value.length; i++)
			getBillCardPanel().getHeadItem(key[i]).setValue(value[i]);
	}

	public String getHeadItemStrValue(String itemKey) {
		BillItem headItem = getBillCardPanel().getHeadItem(itemKey);
		if ((headItem != null) && (headItem.getValueObject() != null)) {
			if ((headItem.getValueObject() instanceof String)) {
				return (String) headItem.getValueObject();
			}
			return ((Integer) headItem.getValueObject()).toString();
		}

		return null;
	}

	public UIRefPane getHeadItemUIRefPane(String key) {
		JComponent component = getBillCardPanel().getHeadItem(key)
				.getComponent();
		return (component instanceof UIRefPane) ? (UIRefPane) component : null;
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

	public String getPk_org() {
		String pk_org = (String) getBillCardPanel().getHeadItem("pk_org")
				.getValueObject();

		if (pk_org == null) {
			return BXUiUtil.getBXDefaultOrgUnit();
		}

		return pk_org;
	}

	private String getNodeCode() {
		return getContext().getNodeCode();
	}

	private LoginContext getContext() {
		return this.editor.getModel().getContext();
	}

	private BillCardPanel getBillCardPanel() {
		return this.editor.getBillCardPanel();
	}

	public HeadFieldHandleUtil getHeadFieldHandle() {
		return this.headFieldHandle;
	}

	public EventHandleUtil getEventHandleUtil() {
		return this.eventUtil;
	}

	public void bodyRowChange(BillEditEvent e) {
	}

	public void valueChanged(ValueChangedEvent event) {
		if (!isConfirmChangedOrg(event)) {
			return;
		}

		Object newValue = event.getNewValue();
		String newpk_org = null;
		if ((newValue instanceof String[])) {
			newpk_org = ((String[]) (String[]) newValue)[0];
		}

		try {
			getBillCardPanel().getHeadItem("pk_org").setValue(newpk_org);
			afterEditPk_org();

			getBillCardPanel().getHeadItem(JKBXHeaderVO.PK_PAYORG).setValue(
					newpk_org);
			afterEditOrgField(JKBXHeaderVO.PK_PAYORG);

			this.editor.doReimRuleAction();
		} catch (BusinessException e1) {
			ShowStatusBarMsgUtil.showErrorMsg(
					NCLangRes4VoTransl.getNCLangRes().getStrByID("201107_0",
							"0201107-0035"),
					NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"expensepub_0", "02011002-0001"), this.editor
							.getModel().getContext());

			nc.vo.er.exception.ExceptionHandler.handleExceptionRuntime(e1);
		}
	}

	private boolean isConfirmChangedOrg(ValueChangedEvent event) {
		Object oldValue = event.getOldValue();
		if (oldValue == null) {
			this.editor.setEditable(true);
			return true;
		}

		if (MessageDialog.showYesNoDlg(
				this.editor,
				NCLangRes4VoTransl.getNCLangRes().getStrByID("upp2012v575_0",
						"0upp2012V575-0128"),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102",
						"UPP2006030102-001123")) == 4) {
			Object newValue = event.getNewValue();
			if ((newValue == null)
					&& (!this.editor.getModel().getContext().getNodeCode()
							.endsWith("20110CBSG")))
				this.editor.getBillCardPanel().setEnabled(false);
			else {
				this.editor.getBillCardPanel().setEnabled(true);
			}

			clearVerifyPage();
			return true;
		}
		String oldpk_org_v = null;
		if ((oldValue instanceof String[])) {
			oldpk_org_v = ((String[]) (String[]) oldValue)[0];
		}
		this.editor.getBillOrgPanel().getRefPane().setPK(oldpk_org_v);
		return false;
	}

	private boolean isBxBill() {
		return "bx".equals(getHeadItemStrValue("djdl"));
	}

	private boolean isPersonPaytarget() {
		Integer paytarget = (Integer) getHeadValue("paytarget");
		if ((paytarget == null) || (paytarget.intValue() == 0)) {
			return true;
		}
		return false;
	}

	private boolean isImporting() {
		return getBillCardPanel().getBillData().isImporting();
	}
}