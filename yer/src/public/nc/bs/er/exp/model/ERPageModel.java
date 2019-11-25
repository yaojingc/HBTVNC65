package nc.bs.er.exp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import nc.bs.er.exp.quickshare.listeners.ref.ShareRuleRefListener;
import nc.bs.er.exp.util.ExpRegisterUtil;
import nc.bs.er.exp.util.ExpUtil;
import nc.bs.er.util.YerMenuUtil;
import nc.bs.er.util.YerUtil;
import nc.bs.erm.util.ErUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.itf.arap.prv.IBXBillPrivate;
import nc.itf.er.pub.IArapBillTypePublic;
import nc.itf.er.reimtype.IReimTypeService;
import nc.itf.erm.ntb.IErmLinkBudgetService;
import nc.itf.fi.pub.Currency;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.AppSession;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelations;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.data.UnmodifiableMdField;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.FormRule;
import nc.uap.lfw.core.event.conf.ViewRule;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.formular.LfwFormulaParser;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.MasterFieldInfo;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIView;
import nc.ui.bd.ref.RefPubUtil;
import nc.vo.bd.ref.RefInfoVO;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.er.linkntb.LinkNtbParamVO;
import nc.vo.er.reimrule.ReimRuleDimVO;
import nc.vo.er.reimrule.ReimRulerVO;
import nc.vo.fipub.rulecontrol.RuleDataCacheEx;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tb.obj.NtbParamVO;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import org.apache.commons.lang.StringUtils;
import uap.web.bd.pub.AppUtil;

public abstract class ERPageModel extends PageModel {
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();

		UIMeta uiMeta = (UIMeta) getUIMeta();

		String includejs = uiMeta.getIncludejs();
		if (!StringUtils.isEmpty(includejs)) {
			includejs = includejs + ",";
		} else {
			includejs = "";
		}

		Boolean isCopy = Boolean
				.valueOf(LfwRuntimeEnvironment.getWebContext().getAppSession()
						.getOriginalParameter("isCopy") == null ? false
						: Boolean.valueOf(
								LfwRuntimeEnvironment.getWebContext()
										.getAppSession()
										.getOriginalParameter("isCopy"))
								.booleanValue());

		AppUtil.addAppAttr("isCopy", isCopy);

		uiMeta.setIncludejs(includejs
				+ "../sync/yer/weberm/html/nodes/includejs/themes/webclassic/ermybill/erbill.js");

		String flowTypePk = LfwRuntimeEnvironment.getWebContext()
				.getAppSession().getOriginalParameter("billType");
		String taskPk = LfwRuntimeEnvironment.getWebContext().getAppSession()
				.getOriginalParameter("taskPk");
		AppUtil.addAppAttr("$$$$$$$$FLOWTYPEPK", flowTypePk);
		AppUtil.addAppAttr("$$$$$$$$TaskPk", taskPk);
		String billId = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("openBillId");
		AppUtil.addAppAttr("billId", billId);
		AppUtil.addAppAttr("NC", "Y");

		String islinkjkd = LfwRuntimeEnvironment.getWebContext()
				.getAppSession().getOriginalParameter("islinkjkd");
		AppUtil.addAppAttr("IS_LINKJKD", islinkjkd);

		if ("Y".equals(islinkjkd)) {
			LfwView menuWidget = getPageMeta().getView("jk_menu");
			MenubarComp menuComp = menuWidget.getViewMenus().getMenuBar(
					"jkzb_menu");
			List<MenuItem> menuList = menuComp.getMenuList();

			for (MenuItem menu : menuList) {
				if ((!"filemanager".equals(menu.getId()))
						&& (!"link".equals(menu.getId()))) {
					menu.setEnabled(false);
					menu.setStateManager(null);
				}
			}
		}

		String islinkbxd = LfwRuntimeEnvironment.getWebContext()
				.getAppSession().getOriginalParameter("islinkbxd");
		AppUtil.addAppAttr("IS_LINKBXD", islinkbxd);
		if ("Y".equals(islinkbxd)) {
			LfwView menuWidget = getPageMeta().getView("bx_menu");
			MenubarComp menuComp = menuWidget.getViewMenus().getMenuBar(
					"bxzb_menu");
			List<MenuItem> menuList = menuComp.getMenuList();

			for (MenuItem menu : menuList) {
				if ((!"filemanager".equals(menu.getId()))
						&& (!"link".equals(menu.getId()))
						&& (!"otheroperate".equals(menu.getId()))) {
					menu.setEnabled(false);
					menu.setStateManager(null);
				} else if ("otheroperate".equals(menu.getId())) {
					List<MenuItem> childList = menu.getChildList();
					for (MenuItem childmMenu : childList) {
						if (!"filemanager".equals(childmMenu.getId())) {
							childmMenu.setEnabled(false);
							childmMenu.setStateManager(null);
						}
					}
				}
			}
		}

		String appName = (String) LfwRuntimeEnvironment.getWebContext()
				.getAppSession().getAttribute("appId");
		String nodecode = (String) LfwRuntimeEnvironment.getWebContext()
				.getAppSession().getAttribute("nodecode");
		AppUtil.addAppAttr("DJURL", "app/" + appName + "?billType="
				+ flowTypePk + "&nodecode=" + nodecode);

		AppUtil.addAppAttr("CURRENT_MASTER_DS", getDatasetID());

		DjLXVO djlxvo = getDjLXVOAndCheck(flowTypePk);
		BilltypeVO billtypevo = PfDataCache.getBillType(flowTypePk);

		AppUtil.addAppAttr(
				"EXP_DJLXMC",
				billtypevo == null ? djlxvo.getDjlxmc() : billtypevo
						.getBilltypenameOfCurrLang());
		AppUtil.addAppAttr("IsAdjust", "N");
		if ((djlxvo.getBxtype() != null)
				&& (2 == djlxvo.getBxtype().intValue())) {
			AppUtil.addAppAttr("IsAdjust", "Y");
		}

		AppUtil.addAppAttr("yerdjdl", djlxvo.getDjdl());

		UFBoolean is_mactrl = djlxvo.getIs_mactrl();
		if ((is_mactrl != null) && (is_mactrl.booleanValue())) {
			AppUtil.addAppAttr("exp_is_mactrl", "Y");
		} else {
			AppUtil.addAppAttr("exp_is_mactrl", null);
		}

		String[] psnArr = getPersonInfo();
		String pk_org = getDefaultPermissionOrg(psnArr[2]);

		AppUtil.addAppAttr("psn_jkbxr", psnArr[0]);
		AppUtil.addAppAttr("psn_dept", psnArr[1]);
		AppUtil.addAppAttr("psn_org", pk_org);
		AppUtil.addAppAttr("psn_no_permission_org", psnArr[2]);
		AppUtil.addAppAttr("psn_group", psnArr[3]);
		AppUtil.addAppAttr("default_bzbm", getDefaultCurrency(pk_org, djlxvo));
		AppUtil.addAppAttr("default_djlxbm", flowTypePk);

		LfwWindow pageMeta = getPageMeta();
		LfwView mainWidget = pageMeta.getView("main");

		String dsID = getDatasetID();
		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);

		saveBusitemDsFormular(mainWidget);

		moidfyMainViewComps(mainWidget);

		if (((djlxvo.getBxtype() == null) || ((djlxvo.getBxtype() != null) && (2 != djlxvo
				.getBxtype().intValue())))
				&& (!"2647".equals(djlxvo.getDjlxbm()))) {
			AppUtil.addAppAttr("exp_reimrule_flag", "Y");
			getReimRuleDataMap(masterDs);
		}

		moidfyMainViewModels(mainWidget);

		moidfyOtherViews(pageMeta);

		if ("2647".equals(flowTypePk)) {
			dealHKBill(pageMeta, uiMeta);
		}

		dealOpenfromXYZ(pageMeta, uiMeta);

		ExpRegisterUtil.registerListenerAll(this);

		ExpRegisterUtil.registerListener(this);

		boolean hasBusitemGrid = ExpUtil.hasBusitemGrid(mainWidget, masterDs);
		boolean hasBusiGridInUIMeta = ExpUtil.isHasBusiGridInUIMeta();
		if ((!hasBusitemGrid) || (!hasBusiGridInUIMeta)) {
			AppUtil.addAppAttr("ExpHasBusitemGrid", "N");
		}

		LfwRuntimeEnvironment.getWebContext().getRequest().getSession()
				.removeAttribute("cjk_selectedJKHeadVOsList");

		LfwRuntimeEnvironment.getWebContext().getRequest().getSession()
				.removeAttribute("addfrommt_selected_mtheadvo");

		RuleDataCacheEx.getInstance();
		RuleDataCacheEx.getRuledatamap().clear();
		RuleDataCacheEx.getInstance();
		RuleDataCacheEx.getRulesmap().clear();
		RuleDataCacheEx.getInstance();
		RuleDataCacheEx.getBillruleMap().clear();
		RuleDataCacheEx.getInstance();
		RuleDataCacheEx.getFactorruleMap().clear();
		RuleDataCacheEx.getInstance();
		RuleDataCacheEx.getAccasoaruleMap().clear();
		RuleDataCacheEx.getInstance();
		RuleDataCacheEx.getBusinormruleMap().clear();
		RuleDataCacheEx.getInstance();
		RuleDataCacheEx.getRule_assmapMap().clear();
		RuleDataCacheEx.getInstance().getRule_assid_valMap().clear();
		RuleDataCacheEx.getInstance();
		RuleDataCacheEx.getItembindmap().clear();
	}

	private String[] getPersonInfo() {
		String[] str = { null, null, null, null };
		try {
			str = ((IBXBillPrivate) NCLocator.getInstance().lookup(
					IBXBillPrivate.class)).queryPsnidAndDeptid(
					YerUtil.getPk_user(), YerUtil.getPK_group());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		return str;
	}

	private String getDefaultCurrency(String pk_org, DjLXVO djlxvo) {
		String org = pk_org == null ? "" : pk_org;

		String defcurrency = djlxvo.getDefcurrency();
		if (((defcurrency == null) || (defcurrency.trim().length() == 0))
				&& (org != null) && (org.length() != 0)) {
			try {
				defcurrency = Currency.getOrgLocalCurrPK(org);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new LfwRuntimeException(e.getMessage());
			}
		}

		return defcurrency;
	}

	private String getDefaultPermissionOrg(String defaultOrg) {
		String pk_org = null;
		try {
			if ((defaultOrg != null) && (defaultOrg.length() > 0)) {
				String[] values = ExpUtil.getPermissionOrgsPortal();
				if ((values != null) && (values.length > 0)) {
					List<String> permissionOrgList = Arrays.asList(values);
					if (permissionOrgList.contains(defaultOrg)) {
						pk_org = defaultOrg;
					}
				}
			}
		} catch (CpbBusinessException e) {
			Logger.error(e.getMessage(), e);
			pk_org = defaultOrg;
		}
		return pk_org;
	}

	private DjLXVO getDjLXVOAndCheck(String billType) {
		DjLXVO djLXVO = null;
		try {
			djLXVO = ((IArapBillTypePublic) NCLocator.getInstance().lookup(
					IArapBillTypePublic.class)).getDjlxvoByDjlxbm(billType,
					YerUtil.getPK_group());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		if (djLXVO == null) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("COMMON", "UPP2011-000171"));
		}

		UFBoolean fcbz = djLXVO.getFcbz();
		if ((fcbz != null) && (fcbz.booleanValue())) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("COMMON", "UPP2011-000429"));
		}

		return djLXVO;
	}

	private void moidfyMainViewComps(LfwView mainWidget) {
		String djzt = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("djzt");
		Boolean isCopy = Boolean
				.valueOf(LfwRuntimeEnvironment.getWebContext().getAppSession()
						.getOriginalParameter("isCopy") == null ? false
						: Boolean.valueOf(
								LfwRuntimeEnvironment.getWebContext()
										.getAppSession()
										.getOriginalParameter("isCopy"))
								.booleanValue());

		if ((djzt != null) && (!"0".equals(djzt)) && (!"1".equals(djzt))
				&& (!isCopy.booleanValue())) {
			WebComponent[] components = mainWidget.getViewComponents()
					.getComponents();
			if (components != null) {
				for (WebComponent com : components) {
					if ((com instanceof FormComp)) {
						((FormComp) com).setRenderType(6);
						((FormComp) com).setLabelMinWidth(153);
					}

					if ((com instanceof GridComp)) {
						((GridComp) com).setShowImageBtn(false);
					}
				}
			}
		} else {
			WebComponent[] components = mainWidget.getViewComponents()
					.getComponents();
			if (components != null) {
				for (WebComponent com : components) {
					if ((com instanceof FormComp)) {
						((FormComp) com).setRenderType(5);
						((FormComp) com).setLabelMinWidth(153);
					}
				}
			}
		}

		WebComponent[] components = mainWidget.getViewComponents()
				.getComponents();
		if (components != null) {
			for (WebComponent com : components) {
				if ((com instanceof GridComp)) {

					String gridDsId = ((GridComp) com).getDataset();
					Dataset gridDs = mainWidget.getViewModels().getDataset(
							gridDsId);

					for (IGridColumn column : ((GridComp) com).getColumnList()) {
						GridColumn thisColumn = (GridColumn) column;

						if ("DecimalText".equals(thisColumn.getEditorType())) {
							Field field = gridDs.getFieldSet().getField(
									thisColumn.getField());

							if (field != null) {
								YerUtil.modifyField(gridDs, "Precision",
										field.getId(), "8");
							}
						}

						if ("IntegerText".equals(thisColumn.getEditorType())) {

							Field field = gridDs.getFieldSet().getField(
									thisColumn.getField());
							if (field != null) {
								YerUtil.modifyField(gridDs, "Precision",
										field.getId(), "-1");
							}
						}
					}

					if (("contrast_grid".equals(com.getId()))
							|| ("bx_accrued_verify_grid".equals(com.getId()))) {
						((GridComp) com).setShowImageBtn(false);
					} else {
						((GridComp) com).setShowImageBtn(true);

						MenubarComp menuBarComp = ((GridComp) com).getMenuBar();
						if (menuBarComp != null) {
							List<MenuItem> menuList = menuBarComp.getMenuList();
							for (int i = 0; i < menuList.size(); i++) {
								MenuItem item = (MenuItem) menuList.get(i);

								if (item.getId().endsWith("HeaderBtn_Edit")) {
									menuList.remove(i);
									i--;
								}

								if (item.getId().endsWith("HeaderBtn_Add")) {
									menuList.remove(i);
									i--;
								}

								if (item.getId().endsWith("HeaderBtn_Delete")) {
									menuList.remove(i);
									i--;
								}
							}
						}
						initExpGridMenubar((GridComp) com);
					}
				}
			}
		}

		addBusitemGridEvent(mainWidget);
	}

	private void moidfyMainViewModels(LfwView mainWidget) {
		String dsID = getDatasetID();
		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);

		String[] fieldStr = { "iscostshare", "isexpamt", "isexpedited",
				"isneedimag" };

		for (int i = 0; i < fieldStr.length; i++) {
			Field tField = masterDs.getFieldSet().getField(fieldStr[i]);
			if (tField != null) {
				if (("UFBoolean".equals(tField.getDataType()))
						&& ("N".equals(tField.getDefaultValue()))) {
					if ((tField instanceof UnmodifiableMdField)) {
						Field newField = ((UnmodifiableMdField) tField)
								.getMDField();
						newField.setDefaultValue(UFBoolean.FALSE);
						masterDs.getFieldSet().updateField(newField.getId(),
								newField);
					} else {
						tField.setDefaultValue(UFBoolean.FALSE);
					}
				} else if (("UFBoolean".equals(tField.getDataType()))
						&& ("Y".equals(tField.getDefaultValue()))) {
					if ((tField instanceof UnmodifiableMdField)) {
						Field newField = ((UnmodifiableMdField) tField)
								.getMDField();
						newField.setDefaultValue(UFBoolean.TRUE);
						masterDs.getFieldSet().updateField(newField.getId(),
								newField);
					} else {
						tField.setDefaultValue(UFBoolean.TRUE);
					}
				}
			}
		}

		Field field = masterDs.getFieldSet().getField("zy");
		if (field != null) {
			field.setExtendAttribute(Field.MAX_LENGTH, "256");
		}
		RefNode zyRef = (RefNode) mainWidget.getViewModels().getRefNode(
				"refnode_bxzb_zy_summaryname");
		if (zyRef == null) {
			zyRef = (RefNode) mainWidget.getViewModels().getRefNode(
					"refnode_jkzb_zy_summaryname");
		}

		if (zyRef != null) {
			zyRef.setReadFields("summaryname,summaryname");
			masterDs.getFieldRelations().removeFieldRelation("zy_mc_rel");
			masterDs.getFieldRelations().removeFieldRelation("zy_rel");
			zyRef.setAllowInput(true);
		}

		List<EventConf> eventConfList = mainWidget.getEventConfList();
		if (eventConfList != null) {
			EventConf eventConf = (EventConf) eventConfList.get(0);
			if ((eventConf != null)
					&& ("pluginplugin_exetask"
							.equals(eventConf.getMethodName()))) {
				eventConf.setName("approvePlugin");
				addWebElementEvent(mainWidget, "approvePlugin",
						"pluginplugin_exetask", null);
			}
		}

		addWebElementEvent(masterDs, "onAfterDataChange",
				"onAfterZBDataChange",
				"nc.uap.lfw.core.event.conf.DatasetListener");
		addWebElementEvent(masterDs, "onDataLoad", "onDataLoad",
				"nc.uap.lfw.core.event.conf.DatasetListener");
		addWebElementEvent(masterDs, "onAfterRowSelect", "onAfterRowSelect",
				"nc.uap.lfw.core.event.conf.DatasetListener");

		Dataset[] busitemDss = ExpUtil
				.getBusitemDss(mainWidget, getDatasetID());
		if (busitemDss != null) {
			for (Dataset busitemDs : busitemDss) {
				addWebElementEvent(busitemDs, "onAfterDataChange",
						"onAfterBusitemDsChange",
						"nc.uap.lfw.core.event.conf.DatasetListener");
				addWebElementEvent(busitemDs, "onAfterRowDelete",
						"onAfterBusitemDsRowDelete",
						"nc.uap.lfw.core.event.conf.DatasetListener");
				addWebElementEvent(busitemDs, "onAfterRowInsert",
						"onAfterBusitemDsRowInsert",
						"nc.uap.lfw.core.event.conf.DatasetListener");
			}
		}

		Dataset cShareDs = mainWidget.getViewModels().getDataset(
				"bx_cshare_detail");
		if (cShareDs != null) {
			Field cShareField = cShareDs.getFieldSet().getField("pk_costshare");
			if (cShareField != null) {
				if ((cShareField instanceof UnmodifiableMdField)) {
					Field newField = ((UnmodifiableMdField) cShareField)
							.getMDField();
					newField.setNullAble(true);
					cShareDs.getFieldSet().updateField(newField.getId(),
							newField);
				} else {
					cShareField.setNullAble(true);
				}
			}
		}

		String bxdj = (String) AppUtil.getAppAttr("exp_reimrule_flag");
		EventConf eventConf = masterDs.getEventConf("onAfterDataChange",
				"onAfterZBDataChange");

		String[] changFields = {JKBXHeaderVO.BBHL,JKBXHeaderVO.BZBM,JKBXHeaderVO.DEPTID,JKBXHeaderVO.DEPTID_V,JKBXHeaderVO.DJRQ,
				JKBXHeaderVO.DWBM,JKBXHeaderVO.DWBM_V,JKBXHeaderVO.FYDEPTID_V,JKBXHeaderVO.FYDWBM,JKBXHeaderVO.FYDWBM_V,
				JKBXHeaderVO.ISCOSTSHARE,JKBXHeaderVO.ISEXPAMT,JKBXHeaderVO.JKBXR,JKBXHeaderVO.PK_ORG,JKBXHeaderVO.PK_ORG_V,
				JKBXHeaderVO.PK_PAYORG_V,JKBXHeaderVO.PK_PCORG_V,JKBXHeaderVO.RECEIVER,JKBXHeaderVO.SZXMID,JKBXHeaderVO.TOTAL,
				JKBXHeaderVO.YBJE,JKBXHeaderVO.JOBID,JKBXHeaderVO.HBBM,JKBXHeaderVO.CUSTOMER,JKBXHeaderVO.PROJECTTASK,
				JKBXHeaderVO.PK_PCORG,JKBXHeaderVO.PK_CHECKELE,JKBXHeaderVO.PK_RESACOSTCENTER,JKBXHeaderVO.FYDEPTID,
				JKBXHeaderVO.CASHPROJ,JKBXHeaderVO.FREECUST,JKBXHeaderVO.GROUPBBHL,JKBXHeaderVO.GLOBALBBHL,/*JKBXHeaderVO.ISCUSUPPLIER,*/
				JKBXHeaderVO.CUSTACCOUNT,JKBXHeaderVO.SKYHZH,
				
				
				//Ìí¼Ó¼àÌý×Ö¶Î,×Ô¶¨ÒåÏî¼àÌý×Ö¶Î
				
				JKBXHeaderVO.ZYX1,
				
				JKBXHeaderVO.PAYTARGET};

		String changFieldStr = "";
		for (int i = 0; i < changFields.length; i++) {
			changFieldStr = changFieldStr + changFields[i];
			changFieldStr = changFieldStr + ",";
		}
		changFieldStr = changFieldStr.substring(0, changFieldStr.length() - 1);
		if ((bxdj != null) && ("Y".equals(bxdj))) {
			Map<String, String> ReimRuleMap = (Map) LfwRuntimeEnvironment
					.getWebContext().getRequest().getSession()
					.getAttribute("yer_ReimRuleHeadMap");
			if ((ReimRuleMap != null) && (ReimRuleMap.size() > 0)) {
				Set<String> keySet1 = ReimRuleMap.keySet();
				for (String key : keySet1) {
					changFieldStr = changFieldStr + ",";
					changFieldStr = changFieldStr + key;
				}
			}
		}
		LfwParameter parameter = new LfwParameter("dataset_field_id",
				changFieldStr);
		eventConf.getExtendParamList().add(parameter);
		eventConf.addExtendParam(parameter);

		String busitemDsID = "";
		if ("bxzb".equals(getDatasetID())) {
			busitemDsID = "busitem";
		} else {
			busitemDsID = "jk_busitem";
		}

		Map<String, String> formularMap = (HashMap) LfwRuntimeEnvironment
				.getWebContext().getRequest().getSession()
				.getAttribute("yer_formularMap");
		Set<String> keySet = formularMap.keySet();
		//TODO:
		//Ìí¼Ó¼àÌý×Ö¶Î add by yanyan 2019-09-09
		//defitem7  "szxmid","defitem50","defitem37","defitem42","defitem36","defitem48","defitem43","defitem44",
		String busitemChangFieldStr = "amount,szxmid,defitem50,defitem37,defitem42,defitem36,defitem48,defitem43,defitem44,ybje,pk_reimtype,jobid,pk_pcorg,pk_pcorg_v,dwbm,deptid,jkbxr,paytarget,receiver,skyhzh,hbbm,customer,custaccount,defitem8,defitem7";
		for (String key : keySet) {
			busitemChangFieldStr = busitemChangFieldStr + ",";
			busitemChangFieldStr = busitemChangFieldStr + key;
		}

		if ((bxdj != null) && ("Y".equals(bxdj))) {
			Map<String, String> ReimRuleMap = (Map) LfwRuntimeEnvironment
					.getWebContext().getRequest().getSession()
					.getAttribute("yer_ReimRuleBusitemMap");
			if ((ReimRuleMap != null) && (ReimRuleMap.size() > 0)) {
				Set<String> keySet1 = ReimRuleMap.keySet();
				for (String key : keySet1) {
					busitemChangFieldStr = busitemChangFieldStr + ",";
					busitemChangFieldStr = busitemChangFieldStr + key;
				}
			}
		}
		Dataset busitemDs = mainWidget.getViewModels().getDataset(busitemDsID);
		EventConf busitemDsChangeEventConf = busitemDs.getEventConf(
				"onAfterDataChange", "onAfterBusitemDsChange");
		LfwParameter busitemParameter = new LfwParameter("dataset_field_id",
				busitemChangFieldStr);
		busitemDsChangeEventConf.getExtendParamList().add(busitemParameter);
		busitemDsChangeEventConf.addExtendParam(busitemParameter);

		if (cShareDs != null) {
			EventConf cShareDsChangeEventConf = cShareDs.getEventConf(
					"onAfterDataChange", "onAfterCshareDsChange");
			LfwParameter cShareDsParameter = new LfwParameter(
					"dataset_field_id",
					"assume_org,assume_dept,assume_amount,jobid,pk_pcorg,bbhl,groupbbhl,globalbbhl");
			if (cShareDsChangeEventConf != null) {
				cShareDsChangeEventConf.addExtendParam(cShareDsParameter);
			}
		}

		UIFlowvPanel tbbdetailpanel = ExpUtil.getUIFlowvPanel("tbbdetailpanel");
		if (tbbdetailpanel != null) {
			tbbdetailpanel.setVisible(false);
		}
	}

	private void moidfyOtherViews(LfwWindow pageMeta) {
		LfwView jkbxMenuWidget = null;
		if ("Y".equals((String) AppUtil.getAppAttr("IsAdjust"))) {
			jkbxMenuWidget = pageMeta.getView("fyadjust_menu");
		} else {
			jkbxMenuWidget = pageMeta.getView("bx_menu");
			if (jkbxMenuWidget == null)
				jkbxMenuWidget = pageMeta.getView("jk_menu");
		}
		MenubarComp jkbxMenubar;
		if ("jk_menu".equals(jkbxMenuWidget.getId())) {
			jkbxMenubar = jkbxMenuWidget.getViewMenus().getMenuBar("jkzb_menu");
		} else {
			jkbxMenubar = jkbxMenuWidget.getViewMenus().getMenuBar("bxzb_menu");
		}
		addWebElementEvent(jkbxMenubar.getItem("commit"), "onclick", "commit",
				"nc.uap.lfw.core.event.conf.MouseListener");
		addWebElementEvent(jkbxMenubar.getItem("tempsave"), "onclick",
				"tempSave", "nc.uap.lfw.core.event.conf.MouseListener");
		addWebElementEvent(jkbxMenubar.getItem("print"), "onclick", "print",
				"nc.uap.lfw.core.event.conf.MouseListener");
		addWebElementEvent(jkbxMenubar.getItem("copy"), "onclick", "copy",
				"nc.uap.lfw.core.event.conf.MouseListener");

		addInlineAdvqueryWinConnector(jkbxMenuWidget);

		LfwView fysqlistView = pageMeta.getView("fysqlist");
		addInlineAdvqueryWinConnector(fysqlistView);

		LfwView mainView = pageMeta.getView("main");
		addInlineAdvqueryWinConnector(mainView);

		LfwView hxytView = pageMeta.getView("hxyt");
		addInlineAdvqueryWinConnector(hxytView);

		if (fysqlistView != null) {
			Dataset fysqzb = fysqlistView.getViewModels().getDataset("fysqzb");
			Field pk_tradetype_text = fysqzb.getFieldSet().getField(
					fysqzb.nameToIndex("pk_tradetype_text"));
			if (pk_tradetype_text != null) {
				String billtypename = ExpUtil
						.getCurrentLangNameColumn("billtypename");
				String formular = "pk_tradetype_text->getColValue2(bd_billtype,"
						+ billtypename
						+ ","
						+ "pk_billtypecode ,pk_tradetype,pk_group,pk_group)";
				pk_tradetype_text.setLoadFormular(formular);
			}
		}

		LfwView appRoveFysqInfoView = pageMeta.getView("approvefysqinfo");
		if (appRoveFysqInfoView != null) {
			Dataset fysqzb = appRoveFysqInfoView.getViewModels().getDataset(
					"fysqzb");
			Field pk_tradetype_text = fysqzb.getFieldSet().getField(
					fysqzb.nameToIndex("pk_tradetype_text"));
			if (pk_tradetype_text != null) {
				String billtypename = ExpUtil
						.getCurrentLangNameColumn("billtypename");
				String formular = "pk_tradetype_text->getColValue2(bd_billtype,"
						+ billtypename
						+ ","
						+ "pk_billtypecode ,pk_tradetype,pk_group,pk_group)";
				pk_tradetype_text.setLoadFormular(formular);
			}
		}

		if (hxytView != null) {
			Dataset ytzb = hxytView.getViewModels().getDataset("ytzb");
			Field pk_tradetype_text = ytzb.getFieldSet().getField(
					ytzb.nameToIndex("pk_tradetype_text"));
			if (pk_tradetype_text != null) {
				String billtypename = ExpUtil
						.getCurrentLangNameColumn("billtypename");
				String formular = "pk_tradetype_text->getColValue2(bd_billtype,"
						+ billtypename
						+ ","
						+ "pk_billtypecode ,pk_tradetype,pk_group,pk_group)";
				pk_tradetype_text.setLoadFormular(formular);
			}
		}

		LfwView quickShareView = pageMeta.getView("quickshare");

		if (quickShareView != null) {
			NCRefNode quickShareRefNode = (NCRefNode) quickShareView
					.getViewModels().getRefNode("refnode_quickshare_rule");
			if (quickShareRefNode != null) {
				quickShareRefNode.setDataListener(ShareRuleRefListener.class
						.getName());
			}
		}
	}

	private void dealOpenfromXYZ(LfwWindow pageMeta, UIMeta uiMeta) {
		Logger.info("nc.bs.er.exp.model.ERPageModel.dealOpenfromXYZ start");

		String pageFlag = LfwRuntimeEnvironment.getWebContext().getParameter(
				"sourcePage");
		AppUtil.addAppAttr("pageFlag", pageFlag);

		Logger.info("pageFlag=" + pageFlag);

		String actionType = LfwRuntimeEnvironment.getWebContext().getParameter(
				"actiontype");
		if (actionType == null) {
		}

		AppUtil.addAppAttr("actionType", actionType);

		String isfromssc = LfwRuntimeEnvironment.getWebContext().getParameter(
				"isfromssc");
		AppUtil.addAppAttr("isfromssc", isfromssc);

		String iseditssc = LfwRuntimeEnvironment.getWebContext().getParameter(
				"iseditssc");

		LfwView menuView = YerMenuUtil.getMenuView("jkbx");
		try {
			boolean canModify = YerMenuUtil.getMenuPermission("save", menuView);
			String state = (String) AppUtil.getAppAttr("NCState");

			if ("State_End".equals(state)) {
				AppUtil.addAppAttr("canModify", Boolean.valueOf(false));
			} else {
				AppUtil.addAppAttr("canModify", Boolean.valueOf(canModify));
			}

			if ("N".equals(iseditssc)) {
				AppUtil.addAppAttr("canModify", Boolean.valueOf(false));
			}
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}

		if ((actionType != null) && (pageFlag != null)
				&& ("workflow".equals(pageFlag))
				&& ("MAKEBILL".equals(actionType))) {
			AppUtil.addAppAttr("canModify", Boolean.valueOf(true));
		}

		UIFlowvLayout flowvLayout = (UIFlowvLayout) uiMeta.getElement();
		List<UILayoutPanel> list = flowvLayout.getPanelList();
		Logger.info("pagelist=" + (list == null ? "" : list.toString()));

		if ((pageFlag != null) && ("image".equals(pageFlag))) {
			List<UILayoutPanel> removePanelList = new ArrayList();
			for (UILayoutPanel panel : list) {
				UIView widget = (UIView) panel.getElement();
				if (widget != null) {

					String id = widget.getId();

					if ("pubview_exetask".equals(id)) {
						removePanelList.add(panel);
					}

					if (("bx_menu".equals(id)) || ("jk_menu".equals(id))) {
						LfwView menuWidget = pageMeta.getView(id);

						MenubarComp[] menubars = menuWidget.getViewMenus()
								.getMenuBars();
						List<MenuItem> menuList;
						for (MenubarComp menubar : menubars) {
							menuList = menubar.getMenuList();
							List<MenuItem> removeMenuList = new ArrayList();
							for (MenuItem item : menuList) {
								if (!"filemanager".equals(item.getId())) {
									removeMenuList.add(item);
								}
							}

							for (MenuItem item : removeMenuList) {
								menuList.remove(item);
							}
						}
					}
				}
			}
			for (UILayoutPanel panel : removePanelList) {
				flowvLayout.removePanel(panel);
			}

			LfwView mainWidget = pageMeta.getView("main");
			WebComponent[] components = mainWidget.getViewComponents()
					.getComponents();

			if (components != null) {
				for (WebComponent com : components) {
					if ((com instanceof FormComp)) {
						((FormComp) com).setRenderType(6);
						((FormComp) com).setLabelMinWidth(153);
					}

					if ((com instanceof GridComp)) {
						((GridComp) com).setShowImageBtn(false);

						List<IGridColumn> columnList = ((GridComp) com)
								.getColumnList();

						for (IGridColumn gridColumn : columnList) {
							((GridColumn) gridColumn).setEditable(false);
						}
					}
				}
			}
		}

		MenubarComp menubar;
		if (ExpUtil.isFyadjust()) {
			LfwView menuWidget = getPageMeta().getWidget("fyadjust_menu");
			menubar = menuWidget.getViewMenus().getMenuBar("bxzb_menu");
		} else {
			LfwView menuWidget = getPageMeta().getWidget("bx_menu");
			if (menuWidget == null)
				menuWidget = getPageMeta().getWidget("jk_menu");
			if ("bx_menu".equals(menuWidget.getId())) {
				menubar = menuWidget.getViewMenus().getMenuBar("bxzb_menu");
			} else {
				menubar = menuWidget.getViewMenus().getMenuBar("jkzb_menu");
			}
		}

		if ((pageFlag == null) || (!"workflow".equals(pageFlag))) {
			for (int i = 0; i < list.size(); i++) {
				UILayoutPanel panel = (UILayoutPanel) list.get(i);

				UIElement element = panel.getElement();
				if ((element instanceof UIView)) {
					UIView widget = (UIView) element;
					String id = widget.getId();
					if ("pubview_exetask".equals(id)) {
						flowvLayout.removePanel(panel);
						i--;
					}
					if ("approvefysqinfo".equals(id)) {
						flowvLayout.removePanel(panel);
						i--;
					}
					if ("workflow_task".equals(id)) {
						flowvLayout.removePanel(panel);
						i--;
					}
				}
			}

			MenuItem item = menubar.getItem("save");
			if (item != null) {
				item.setVisible(false);
			}
		}

		if ("workflow".equals(pageFlag)) {
			Integer workflowtype = Integer.valueOf(-1);
			Object o = LfwRuntimeEnvironment.getWebContext().getParameter(
					"workflowtype");
			if (o != null) {
				workflowtype = Integer.valueOf(Integer.parseInt((String) o));
			}

			if (999999 == workflowtype.intValue()) {
				AppUtil.addAppAttr("canModify", Boolean.valueOf(false));
			}

			String billID = LfwRuntimeEnvironment.getWebContext().getParameter(
					"openBillId");

			AppUtil.addAppAttr("billId", billID);
			AppUtil.addAppAttr("workflow_type", workflowtype);

			for (int i = 0; i < list.size(); i++) {
				UILayoutPanel panel = (UILayoutPanel) list.get(i);
				UIElement element = panel.getElement();
				if ((element instanceof UIView)) {
					UIView widget = (UIView) element;
					String id = widget.getId();

					if (workflowtype.intValue() != -1) {
						if ((WorkflowTypeEnum.Approveflow.getIntValue() == workflowtype
								.intValue())
								|| (WorkflowTypeEnum.SubApproveflow
										.getIntValue() == workflowtype
										.intValue())
								|| (WorkflowTypeEnum.SubWorkApproveflow
										.getIntValue() == workflowtype
										.intValue())) {
							if ("workflow_task".equals(id)) {
								flowvLayout.removePanel(panel);
								Logger.info("remove pageId=" + id
										+ " and workflowtype=" + workflowtype);
								i--;
							}
						} else if ("pubview_exetask".equals(id)) {
							flowvLayout.removePanel(panel);
							Logger.info("remove pageId=" + id);
							i--;
						}

						if (((actionType != null) && ("MAKEBILL"
								.equals(actionType)))
								|| ((isfromssc != null) && ("Y"
										.equals(isfromssc)))
								|| ((999999 == workflowtype.intValue()) && (("workflow_task"
										.equals(id))
										|| ("pubview_exetask".equals(id)) || ("approvefysqinfo"
											.equals(id))))) {
							flowvLayout.removePanel(panel);
							Logger.info("remove pageId=" + id
									+ " and workflowtype=" + workflowtype + id
									+ " and isfromssc=" + isfromssc + id
									+ " and actionType=" + actionType);
							i--;
						}
					}
				}
			}

			AppUtil.addAppAttr("pageFlag", "workflow");

			MenuItem item = menubar.getItem("tempsave");
			List<MenuItem> menuList = menubar.getMenuList();
			menuList.remove(item);

			if (isfromssc == null) {

				doApproveYsinfo();
				doApproveFysqinfoDisplay(flowvLayout);
			}
		}

		Logger.info("nc.bs.er.exp.model.ERPageModel.dealOpenfromXYZ end");
	}

	private void dealHKBill(LfwWindow pageMeta, UIMeta uiMeta) {
		LfwView jkbxMenuWidget = pageMeta.getView("bx_menu");
		if (jkbxMenuWidget == null)
			jkbxMenuWidget = pageMeta.getView("jk_menu");
		MenubarComp jkbxMenubar;
		if ("bx_menu".equals(jkbxMenuWidget.getId())) {
			jkbxMenubar = jkbxMenuWidget.getViewMenus().getMenuBar("bxzb_menu");
		} else {
			jkbxMenubar = jkbxMenuWidget.getViewMenus().getMenuBar("jkzb_menu");
		}

		UIFlowvLayout flowvLayout = (UIFlowvLayout) uiMeta.getElement();
		List<UILayoutPanel> list = flowvLayout.getPanelList();

		if (list != null) {
			for (UILayoutPanel panel : list) {
				UIView widget = (UIView) panel.getElement();
				if (widget != null) {

					String id = widget.getId();
					if ("main".equals(id)) {
						UIMeta hkUiMeta = widget.getUimeta();

						UIFlowvLayout hkLayout = (UIFlowvLayout) hkUiMeta.getElement();

						List<UILayoutPanel> removePanelList = new ArrayList();
						List<UILayoutPanel> hkPanelList = hkLayout
								.getPanelList();

						for (UILayoutPanel hkPanel : hkPanelList) {
							if ("panelv10322".equals(hkPanel.getId())) {
								removePanelList.add(hkPanel);
							}
						}
						for (UILayoutPanel ppp : removePanelList)
							hkLayout.removePanel(ppp);
					}
				}
			}
		}
		UIFlowvLayout hkLayout;
		if (jkbxMenubar.getItem("addgroup") != null) {
			List<MenuItem> menuList = jkbxMenubar.getItem("addgroup")
					.getChildList();
			jkbxMenubar.getItem("addgroup").getChildList().removeAll(menuList);

			MenuItem addgroup = jkbxMenubar.getItem("addgroup");
			addgroup.setId("add");
			addWebElementEvent(addgroup, "onclick", "add",
					"nc.uap.lfw.core.event.conf.MouseListener");
		}
		MenuItem cjk = null;
		MenuItem split2 = jkbxMenubar.getItem("split2");
		MenuItem link = jkbxMenubar.getItem("link");
		MenuItem split3 = jkbxMenubar.getItem("split3");
		MenuItem print = jkbxMenubar.getItem("print");
		MenuItem invalid = jkbxMenubar.getItem("invalid");
		MenuItem query = jkbxMenubar.getItem("query");

		if (jkbxMenubar.getItem("otheroperate") != null) {
			List<MenuItem> menuList = jkbxMenubar.getItem("otheroperate")
					.getChildList();
			for (int i = 0; i < menuList.size(); i++) {
				MenuItem item = (MenuItem) menuList.get(i);
				if ("cjk".equals(item.getId())) {
					cjk = (MenuItem) menuList.get(i);
				}
			}
			jkbxMenubar.getItem("otheroperate").getChildList()
					.removeAll(menuList);
		}

		MenuItem otheroperate = jkbxMenubar.getItem("otheroperate");

		jkbxMenubar.getMenuList().remove(split2);
		jkbxMenubar.getMenuList().remove(link);
		jkbxMenubar.getMenuList().remove(split3);
		jkbxMenubar.getMenuList().remove(print);
		jkbxMenubar.getMenuList().remove(invalid);
		jkbxMenubar.getMenuList().remove(query);
		jkbxMenubar.getMenuList().remove(otheroperate);
		jkbxMenubar.getMenuList().add(cjk);
		jkbxMenubar.getMenuList().add(split2);
		jkbxMenubar.getMenuList().add(link);
		jkbxMenubar.getMenuList().add(split3);
		jkbxMenubar.getMenuList().add(print);
		jkbxMenubar.getMenuList().add(invalid);
		jkbxMenubar.getMenuList().add(query);
	}

	private void modifySubmitRule() {
		LfwView menuWidget = getPageMeta().getWidget("bx_menu");
		if (menuWidget == null)
			menuWidget = getPageMeta().getWidget("jk_menu");
		MenubarComp menubar;
		if ("bx_menu".equals(menuWidget.getId())) {
			menubar = menuWidget.getViewMenus().getMenuBar("bxzb_menu");
		} else {
			menubar = menuWidget.getViewMenus().getMenuBar("jkzb_menu");
		}

		EventConf eventConfig = menubar.getItem("tempsave").getEventConf(
				"onclick", "tempSave");

		EventSubmitRule submitRule = eventConfig.getSubmitRule();

		ViewRule wr = submitRule.getWidgetRule("main");

		FormRule fr = new FormRule();
		if ("bx_menu".equals(menuWidget.getId())) {
			fr.setId("bxzb_base_info_form");
		} else {
			fr.setId("jkzb_base_info_form");
		}
		fr.setType("all_child");
		if (wr != null) {
			wr.addFormRule(fr);
		}
	}

	private void addBodyMenuAubmitRule(MenuItem item, String methodName,
			String DsId) {
		EventConf eventConfig = item.getEventConf("onclick", methodName);

		EventSubmitRule submitRule = eventConfig.getSubmitRule();
		if (submitRule == null) {
			submitRule = new EventSubmitRule();
			eventConfig.setSubmitRule(submitRule);
		}

		ViewRule wr = new ViewRule();
		wr.setId("main");

		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(DsId);
		dsRule.setType("ds_current_page");

		wr.addDsRule(dsRule);
		submitRule.addWidgetRule(wr);
	}

	public abstract String getDatasetID();

	public abstract String getMenuViewName();

	private void addRefNodeRelation(String masterField, String slaveFieldId,
			LfwWindow meta) {
		Dataset masterDs = meta.getWidget("main").getViewModels()
				.getDataset(getDatasetID());
		if (masterDs.getFieldSet().getField(slaveFieldId) != null) {
			RefNodeRelation rnr = new RefNodeRelation();
			rnr.setId("relation_" + masterField + "_" + slaveFieldId);
			rnr.setDetailRefNode("refnode_" + getDatasetID() + "_"
					+ slaveFieldId + "_name");
			MasterFieldInfo mfi = new MasterFieldInfo();
			mfi.setDsId(getDatasetID());
			mfi.setFieldId(masterField);
			mfi.setFilterSql("1=1");

			mfi.setNullProcess("ignore");
			rnr.addMasterFieldInfo(mfi);
			RefNodeRelations refNodeRelations = meta.getWidget("main")
					.getViewModels().getRefNodeRelations();

			if (refNodeRelations != null) {
				refNodeRelations.addRefNodeRelation(rnr);
			} else {
				RefNodeRelations newRefNodeRelations = new RefNodeRelations();
				meta.getWidget("main").getViewModels()
						.setRefnodeRelations(newRefNodeRelations);
				newRefNodeRelations.addRefNodeRelation(rnr);
			}
		}
	}

	private void initExpGridMenubar(GridComp gc) {
		MenubarComp menubarComp = gc.getMenuBar();
		if (menubarComp == null) {
			return;
		}
		if (menubarComp.getMenuList().size() > 4) {
			return;
		}
		String[] itemIds = { "new_row", "delete_row", "insert_row", "copy_row",
				"paste_row" };

		String[] eventMethodNames = { "onGridAddClick", "onGridDeleteClick",
				"onGridInsertClick", "onGridCopyClick", "onGridPasteClick" };
		for (int i = 0; i < itemIds.length; i++) {
			MenuItem item = new MenuItem(itemIds[i]);
			item.setI18nName(itemIds[i]);
			item.setTipI18nName(itemIds[i]);
			item.setLangDir("lfwbuttons");

			item.setShowModel(2);

			EventConf event = new EventConf();
			event.setMethodName(eventMethodNames[i]);

			event.setOnserver(true);
			event.setName("onclick");
			item.addEventConf(event);
			gc.getMenuBar().addMenuItem(item);

			addBodyMenuAubmitRule(item, eventMethodNames[i], gc.getDataset());
		}
	}

	private void modifyMasterDsSubmitRule(Dataset masterDs, String eventName,
			String methodName) {
		EventConf event = masterDs.getEventConf(eventName, methodName);
		ViewRule wr;
		if (event != null) {
			EventSubmitRule submitRule = event.getSubmitRule();

			if (submitRule == null) {
				submitRule = new EventSubmitRule();
				event.setSubmitRule(submitRule);
			}

			wr = submitRule.getWidgetRule("main");
			if (wr == null) {
				wr = new ViewRule();
				wr.setId("main");
				submitRule.addWidgetRule(wr);
			}

			List<String> DsID = ExpUtil.getSlaveDsIDs(
					getPageMeta().getWidget("main"), masterDs.getId());
			for (String dsID : DsID) {
				if ((!"contrast".equals(dsID)) && (!"jkcontrast".equals(dsID))) {

					DatasetRule dsRule = new DatasetRule();
					dsRule.setId(dsID);
					dsRule.setType("ds_current_page");
					wr.addDsRule(dsRule);
				}
			}
		}
	}

	protected void addWebElementEvent(WebElement webElement, String eventName,
			String methodName, String JsEventClaszz) {
		if (webElement == null) {
			return;
		}

		EventConf event = webElement.getEventConf(eventName, methodName);
		if (event == null) {
			event = new EventConf();
			event.setMethodName(methodName);

			event.setOnserver(true);
			event.setName(eventName);
			webElement.addEventConf(event);
		}

		EventSubmitRule submitRule = event.getSubmitRule();

		if (submitRule == null) {
			submitRule = new EventSubmitRule();
			event.setSubmitRule(submitRule);
		}

		ViewRule wr = submitRule.getWidgetRule("main");
		if (wr == null) {
			wr = new ViewRule();
			wr.setId("main");
			submitRule.addWidgetRule(wr);
		}

		List<String> DsID = ExpUtil.getSlaveDsIDs(
				getPageMeta().getWidget("main"), getDatasetID());
		for (String dsID : DsID) {
			DatasetRule dsRule = new DatasetRule();
			dsRule.setId(dsID);
			dsRule.setType("ds_current_page");
			wr.addDsRule(dsRule);
		}
	}

	/**
	 * @deprecated
	 */
	protected void addMenuItemEvent(MenuItem menuItem, String eventName,
			String methodName) {
		if (menuItem == null) {
			return;
		}

		EventConf event = menuItem.getEventConf(eventName, methodName);
		if (event == null) {
			event = new EventConf();
			event.setMethodName(methodName);

			event.setOnserver(true);
			event.setName(eventName);
			menuItem.addEventConf(event);
		}

		EventSubmitRule submitRule = event.getSubmitRule();

		if (submitRule == null) {
			submitRule = new EventSubmitRule();
			event.setSubmitRule(submitRule);
		}

		ViewRule wr = submitRule.getWidgetRule("main");
		if (wr == null) {
			wr = new ViewRule();
			wr.setId("main");
			submitRule.addWidgetRule(wr);
		}

		List<String> DsID = ExpUtil.getSlaveDsIDs(
				getPageMeta().getWidget("main"), getDatasetID());
		for (String dsID : DsID) {
			DatasetRule dsRule = new DatasetRule();
			dsRule.setId(dsID);
			dsRule.setType("ds_current_page");
			wr.addDsRule(dsRule);
		}
	}

	private void addBusitemGridEvent(LfwView widget) {
		WebComponent[] wcs = widget.getViewComponents().getComponents();
		for (WebComponent webComp : wcs) {
			if ((webComp instanceof GridComp)) {
				String dsID = ((GridComp) webComp).getDataset();
				if (dsID != null) {

					if ((!"contrast".equals(dsID))
							&& (!"jkcontrast".equals(dsID))) {

						LfwParameter param = new LfwParameter("gridEvent",
								"nc.uap.lfw.core.event.GridEvent");
						EventConf eventConfig = new EventConf(
								"onLastCellEnter", param, null);
						eventConfig.setAsync(true);

						eventConfig.setMethodName("lastCellEnter");
						eventConfig.setOnserver(true);
						EventSubmitRule submitRule = new EventSubmitRule();
						eventConfig.setSubmitRule(submitRule);

						((GridComp) webComp).addEventConf(eventConfig);
					}
				}
			}
		}
	}

	private void addInlineAdvqueryWinConnector(LfwView targetView) {
		if (targetView == null) {
			return;
		}
		PluginDesc pluginDesc = new PluginDesc();
		pluginDesc.setId("conditionQueryPlugin");
		targetView.addPluginDescs(pluginDesc);
		pluginDesc.setMethodName("conditionQueryPlugin");

		WindowConfig winConf = new WindowConfig();
		winConf.setCaption(NCLangRes4VoTransl.getNCLangRes().getStrByID("yer",
				"yer_seniorQuery"));
		winConf.setId("uap.lfw.imp.query.advquery");
		targetView.addInlineWindow(winConf);

		Connector conn = new Connector();
		conn.setId("adv_simple_conn");
		conn.setConnType("6");
		conn.setPluginId("conditionQueryPlugin");
		conn.setPlugoutId("proxy_qryout");
		conn.setSource(winConf.getId());
		conn.setTarget(targetView.getId());
		targetView.addConnector(conn);
	}

	private void saveBusitemDsFormular(LfwView view) {
		String busitemDsID = "";
		if ("bxzb".equals(getDatasetID())) {
			busitemDsID = "busitem";
		} else {
			busitemDsID = "jk_busitem";
		}

		Map<String, String> formularMap = new HashMap();
		Dataset ds = view.getViewModels().getDataset(busitemDsID);
		List<Field> fieldList = ds.getFieldSet().getFieldList();
		for (Field f : fieldList) {
			String editFormular = f.getEditFormular();
			if (editFormular != null) {
				formularMap.put(f.getId(), editFormular);
			}
		}

		LfwRuntimeEnvironment.getWebContext().getRequest().getSession()
				.setAttribute("yer_formularMap", formularMap);
	}

	public void doApproveYsinfo() {
		String djdl = "bxzb".equals(getDatasetID()) ? "bx" : "jk";
		String billId = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("openBillId");

		if ((billId == null) || ("".equals(billId))) {
			return;
		}

		JKBXVO bxvo = null;
		try {
			List<JKBXVO> bxvos = getIBXBillPrivate().queryVOsByPrimaryKeys(
					new String[] { billId }, djdl);
			if ((bxvos == null) || (bxvos.size() == 0))
				return;
			bxvo = (JKBXVO) bxvos.get(0);
		} catch (ComponentException e1) {
			Logger.error(e1.getMessage(), e1);
			throw new LfwRuntimeException(e1);
		} catch (BusinessException e1) {
			Logger.error(e1.getMessage(), e1);
			throw new LfwRuntimeException(e1);
		}

		if (bxvo == null) {
			return;
		}

		boolean istbbused = ErUtil.isProductTbbInstalled("1050");
		if (!istbbused) {
			return;
		}

		String actionCode = getActionCode(bxvo);
		LinkNtbParamVO[] linkNtbParamVO = null;

		try {
			NtbParamVO[] vos = ((IErmLinkBudgetService) NCLocator.getInstance()
					.lookup(IErmLinkBudgetService.class)).getBudgetLinkParams(
					bxvo, actionCode, null);

			if ((null == vos) || (vos.length == 0)) {
				return;
			}

			linkNtbParamVO = convert2WebbxVo(vos);

			AppUtil.addAppAttr("link_ntb_vos_Ysinfo", linkNtbParamVO);
			modifyMeta();
		} catch (Exception e1) {
			Logger.error(e1.getMessage(), e1);
			if ((e1 instanceof LfwRuntimeException)) {
				throw ((LfwRuntimeException) e1);
			}
			throw new LfwRuntimeException(e1);
		}
	}

	private IBXBillPrivate getIBXBillPrivate() throws ComponentException {
		return (IBXBillPrivate) NCLocator.getInstance().lookup(
				IBXBillPrivate.class.getName());
	}

	private String getActionCode(JKBXVO bxvo) {
		JKBXHeaderVO headVO = bxvo.getParentVO();
		int billStatus = headVO.getDjzt().intValue();
		switch (billStatus) {
		case 3:
			return "APPROVE";
		case 2:
			return "APPROVE";
		}
		return "SAVE";
	}

	private LinkNtbParamVO[] convert2WebbxVo(NtbParamVO[] vos) {
		LinkNtbParamVO[] wvos = new LinkNtbParamVO[vos.length];

		int temp = 0;
		NtbParamVO tempVo = null;
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getPkDim().length > temp) {
				tempVo = vos[i];
				temp = vos[i].getPkDim().length;
			}
		}
		AppUtil.addAppAttr("discdim_most", tempVo);
		for (int i = 0; i < vos.length; i++) {
			LinkNtbParamVO lnvo = new LinkNtbParamVO();
			lnvo.setBegindate(vos[i].getBegDate());
			lnvo.setEnddate(vos[i].getEndDate());
			int currtype = vos[i].getCurr_type();
			UFDouble runvalue = vos[i].getRundata()[currtype];
			UFDouble readyvalue = vos[i].getReadydata()[currtype];
			lnvo.setRundata(runvalue.setScale(2, 0));
			lnvo.setReadydata(readyvalue.setScale(2, 0));
			lnvo.setPlanname(vos[i].getPlanname());
			lnvo.setBalance(vos[i].getBalance().setScale(2, 0));

			lnvo.setPlandata(vos[i].getPlanData().setScale(2, 0));

			String[] pkdim = vos[i].getPkDim();
			lnvo.setPkdim(pkdim);

			String[] pkdiscdim = new String[pkdim.length];
			HashMap map = vos[i].getHashDescription();
			for (int k = 0; k < pkdiscdim.length; k++) {
				pkdiscdim[k] = ((String) map.get(pkdim[k]));
			}
			lnvo.setPkdiscdim(pkdiscdim);

			lnvo.setTypedim(vos[i].getTypeDim());
			lnvo.setBusiAttrs(vos[i].getBusiAttrs());
			wvos[i] = lnvo;
		}
		return wvos;
	}

	private void modifyMeta() {
		LfwWindow meta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		GridComp grid = (GridComp) meta.getWidget("approvefysqinfo")
				.getViewComponents().getComponent("ntbGrid");
		Dataset ds = meta.getWidget("approvefysqinfo").getViewModels()
				.getDataset("ntbDs");
		NtbParamVO tempVo = (NtbParamVO) AppUtil.getAppAttr("discdim_most");
		Map<String, String> typesMap = new HashMap();
		if (tempVo != null) {
			String[] attrs = tempVo.getBusiAttrs();
			String[] types = tempVo.getTypeDim();
			int k = 0;
			for (int i = 0; i < attrs.length; i++) {
				if (typesMap.get(types[i]) == null) {

					typesMap.put(types[i], types[i]);

					String id = attrs[i].replace(".", "_");
					GridColumn col = new GridColumn();
					col.setId(id);
					col.setField(id);
					col.setEditorType("StringText");
					col.setDataType("String");
					col.setText(types[i]);

					RefInfoVO vo = RefPubUtil.getRefinfoVO(types[i]);
					if (vo != null) {
						String path = vo.getResidPath();
						String resid = vo.getResid();
						col.setI18nName(resid);
						col.setLangDir(path);
					}

					col.setWidth(80);
					col.setEditable(false);

					grid.getColumnList().add(k, col);

					Field field = new Field();
					field.setId(id);
					field.setField(id);
					field.setDataType("String");
					ds.getFieldSet().addField(field);
					k++;
				}
			}
		}
	}

	public void doApproveFysqinfo() {
		List<String> fysqPkList = new ArrayList();
		String pkStr = "";

		String djdl = "bxzb".equals(getDatasetID()) ? "bx" : "jk";
		String billId = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("openBillId");

		if ((billId == null) || ("".equals(billId))) {
			return;
		}

		JKBXVO bxvo = null;
		try {
			List<JKBXVO> bxvos = getIBXBillPrivate().queryVOsByPrimaryKeys(
					new String[] { billId }, djdl);
			if ((bxvos == null) || (bxvos.size() == 0))
				return;
			bxvo = (JKBXVO) bxvos.get(0);
		} catch (ComponentException e1) {
			Logger.error(e1.getMessage(), e1);
			throw new LfwRuntimeException(e1);
		} catch (BusinessException e1) {
			Logger.error(e1.getMessage(), e1);
			throw new LfwRuntimeException(e1);
		}

		if (bxvo == null) {
			return;
		}
		BXBusItemVO[] busitemArr = bxvo.getBxBusItemVOS();
		if (busitemArr == null) {
			return;
		}
		for (int i = 0; i < busitemArr.length; i++) {
			String pkfysq = busitemArr[i].getPk_item();
			if (pkfysq != null) {
				if (!fysqPkList.contains(pkfysq)) {
					fysqPkList.add(pkfysq);
					pkStr = pkStr + ",,," + pkfysq;
				}
			}
		}

		AppUtil.addAppAttr("Yer_Weberm_ApproveFysqinfo", pkStr);
	}

	public void doApproveFysqinfoDisplay(UIFlowvLayout flowvLayout) {
		if (ExpUtil.isFyadjust()) {
			UIView mainUIView = (UIView) flowvLayout
					.getElementFromPanel("approvefysqinfo");
			if (mainUIView == null)
				return;
			UIMeta mainUIMeta = mainUIView.getUimeta();
			UIFlowvLayout mainFlowvLayout = (UIFlowvLayout) mainUIMeta
					.getElement();
			List<UILayoutPanel> panelList = mainFlowvLayout.getPanelList();
			UIFlowvPanel tempVpanel = null;
			for (UILayoutPanel panel : panelList) {
				UIFlowvPanel vpanel = (UIFlowvPanel) panel;
				if ("approveinfo_ys_fysq".equals(vpanel.getId())) {
					tempVpanel = vpanel;
					break;
				}
			}
			UIFlowvLayout tUIFlowvLayout = (UIFlowvLayout) tempVpanel
					.getElement();
			List<UILayoutPanel> tPanelList = tUIFlowvLayout.getPanelList();

			UIFlowvPanel tUIFlowvPanel = null;
			for (UILayoutPanel panel : tPanelList) {
				UIFlowvPanel vpanel = (UIFlowvPanel) panel;
				if ("flovwPanel2".equals(vpanel.getId())) {
					tUIFlowvPanel = vpanel;
					break;
				}
			}
			tUIFlowvPanel.setVisible(false);
		} else {
			doApproveFysqinfo();
		}
	}

	private void getReimRuleDataMap(Dataset masterDs) {
		Map<String, String> ReimRuleBusitemMap = new HashMap();
		Map<String, String> ReimRuleHeadMap = new HashMap();
		String billtype = (String) AppUtil.getAppAttr("default_djlxbm");
		String pk_org = (String) AppUtil.getAppAttr("psn_org");

		if ((pk_org == null) || (pk_org.trim().length() <= 0)) {
			pk_org = (String) AppUtil.getAppAttr("psn_no_permission_org");
		}
		if (pk_org != null) {
			try {
				List<ReimRuleDimVO> vos = ((IReimTypeService) NCLocator
						.getInstance().lookup(IReimTypeService.class))
						.queryReimDim(billtype, null, pk_org);
				List<ReimRulerVO> rules = ((IReimTypeService) NCLocator
						.getInstance().lookup(IReimTypeService.class))
						.queryReimRuler(billtype, null, pk_org);

				FormulaParse fp = LfwFormulaParser.getInstance();
				Set<String> fomulas = new HashSet();
				if (rules != null) {
					for (ReimRulerVO rule : rules) {
						if (!StringUtils.isEmpty(rule.getControlformula())) {
							fomulas.add(rule.getControlformula());
						}
					}
				}
				if (fomulas.size() > 0) {
					for (String formula : fomulas) {
						LfwFormulaParser.addExpr(formula, fp);
						VarryVO[] varryVOs = fp.getVarryArray();
						if (varryVOs != null) {
							for (VarryVO varryVO : varryVOs) {
								if (varryVO != null) {
									String[] varrays = varryVO.getVarry();
									if (varrays != null) {
										for (String varray : varrays) {
											if (!StringUtils.isEmpty(varray)) {
												ReimRuleBusitemMap.put(varray,
														varray);
											}
										}
									}
								}
							}
						}
					}
				}

				if (vos != null) {
					for (int i = 0; i < vos.size(); i++) {
						ReimRuleDimVO temp = (ReimRuleDimVO) vos.get(i);
						if ((temp.getBillrefcode() != null)
								&& ((temp.getBillrefcode()
										.startsWith("er_busitem")) || (temp
										.getBillrefcode()
										.startsWith("jk_busitem")))) {
							String billrefcode = temp.getBillrefcode();
							if (temp.getBillrefcode().startsWith("er_busitem")) {
								billrefcode = billrefcode
										.substring("er_busitem".length() + 1);
							}
							if (temp.getBillrefcode().startsWith("jk_busitem")) {
								billrefcode = billrefcode
										.substring("jk_busitem".length() + 1);
							}
							ReimRuleBusitemMap.put(billrefcode, billrefcode);
						}
						if ((temp.getBillrefcode() != null)
								&& ((masterDs
										.nameToIndex(temp.getBillrefcode()) > 0)
										|| (temp.getBillrefcode()
												.startsWith("er_bxzb")) || (temp
											.getBillrefcode()
										.startsWith("er_jkzb")))) {
							String billrefcode = temp.getBillrefcode();
							if (billrefcode.startsWith("er_bxzb")) {
								billrefcode = billrefcode.substring("er_bxzb"
										.length() + 1);
							}
							if (billrefcode.startsWith("er_jkzb")) {
								billrefcode = billrefcode.substring("er_jkzb"
										.length() + 1);
							}

							ReimRuleHeadMap.put(billrefcode, billrefcode);
						}
					}
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		LfwRuntimeEnvironment.getWebContext().getRequest().getSession()
				.setAttribute("yer_ReimRuleBusitemMap", ReimRuleBusitemMap);
		LfwRuntimeEnvironment.getWebContext().getRequest().getSession()
				.setAttribute("yer_ReimRuleHeadMap", ReimRuleHeadMap);
	}
}
