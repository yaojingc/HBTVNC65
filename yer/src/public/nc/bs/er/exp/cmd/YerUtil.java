package nc.bs.er.exp.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.er.proxy.ProxyDjlx;
import nc.individuation.property.pub.IndividuationManager;
import nc.itf.arap.prv.IBXBillPrivate;
import nc.itf.er.common.ICommonService;
import nc.itf.org.IOrgUnitQryService;
import nc.pubitf.rbac.IUserPubService;
import nc.pubitf.uapbd.IPsndocPubService;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpAppsNodeQry;
import nc.uap.cpb.org.itf.ICpOrgQry;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.UnmodifiableMdField;
import nc.uap.lfw.core.datamodel.IDatasetProvider;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import uap.web.bd.pub.BDLanguageHelper;

/**
 * Yer通用工具类
 */
public class YerUtil {
	
	
	public static String getPK_group(){
		String pk_group = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		return pk_group;
	}

	public static String getPk_user(){
		return LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
	}
	
	/**
	 * 返回登录日期
	 */
	public static UFDate getLoginDate(){
		//TODO  暂时如下，待有相关方法从 LfwRuntimeEnvironment 中取
		UFDateTime ut = new UFDateTime();
		return new UFDate(ut.getMillis());
	}
	/**
	 * 获取billtype和url对应的map  TODO 6.1 是否放到缓存中以减少访问数据库
	 */
	public static Map<String,String> getBilltypeUrlMap(){
		Map<String,String> billtypeUrlMap = new HashMap<String, String>();
		try {
			ICpAppsNodeQry cpAppsNodeQry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
			String where = " ID like 'E110%'";
			CpAppsNodeVO[] cpAppsNodeVOArr = cpAppsNodeQry.getAppsNodeVos(where);
			if (cpAppsNodeVOArr!=null && cpAppsNodeVOArr.length>0) {
				for (CpAppsNodeVO cpAppsNodeVO : cpAppsNodeVOArr) {
					String erURL = cpAppsNodeVO.getUrl();
					int index = erURL.indexOf("billType=");
					if (index != -1) {
						String billtype = erURL.substring(index+"billType=".length());
						//TODO 6.1 nodecode 是否要加到url上
						String nodeCode = cpAppsNodeVO.getId();
						erURL += "&nodecode="+nodeCode;
						billtypeUrlMap.put(billtype, erURL);
					}
				}
			}

		} catch (CpbBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return billtypeUrlMap;
	}
	/**
	 * 返回业务日期
	 * @return
	 */
	public static UFDate getBusiDate(){
//		NcSessionBean ncSessionBean = (NcSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
		UFDateTime ut = new UFDateTime();
//		if (ncSessionBean.getChageTime() != null) {
//			ut = new UFDateTime(ncSessionBean.getChageTime());
//		}
		return new UFDate(ut.getMillis());
	}
	
	/**
	 * 返回人员信息(array[0]= 人员主键，array[1]=人员所属部门,array[2]=人员所属组织,array[3]=人员所在集团)
	 * @param pk_psndoc 人员组件
	 * @return
	 */
	public static String[] getPsnDocInfoById(String pk_psndoc){
		//retrun BXBsUtil.getPsnDocInfoById(pk_psndoc);
		String[] result = new String[4];
		result[0] = pk_psndoc;
		if (pk_psndoc != null) {
			IPsndocPubService pd = NCLocator.getInstance().lookup(IPsndocPubService.class);
			try {
				PsndocVO[] pm;
				pm = pd.queryPsndocByPks(new String[] { pk_psndoc }, new String[]{PsndocVO.PK_ORG, PsndocVO.PK_GROUP});
				result[1] = pd.queryMainDeptByPandocIDs(pk_psndoc).get("pk_dept");
				result[2] = pm[0].getPk_org();
				result[3] = pm[0].getPk_group();
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return result;
	}
	
	/**
	 * 返回人员信息(array[0]= 人员主键，array[1]=人员所属部门,array[2]=人员所属组织,array[3]=人员所在集团)
	 * @param pk_psndoc 人员组件
	 * @return
	 */
	public static String[] getPsnDocInfo(String cuserid){
		String[] result = new String[4];
		try {
			String jkbxr  = NCLocator.getInstance().lookup(IUserPubService.class).queryPsndocByUserid(cuserid);
			result[0] = jkbxr;
			if (jkbxr != null) {
				IPsndocPubService pd = NCLocator.getInstance().lookup(IPsndocPubService.class);
				PsndocVO[] pm = pd.queryPsndocByPks(new String[] { jkbxr }, new String[]{PsndocVO.PK_ORG, PsndocVO.PK_GROUP});
				result[1] = pd.queryMainDeptByPandocIDs(jkbxr).get("pk_dept");
				result[2] = pm[0].getPk_org();
				result[3] = pm[0].getPk_group();
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 是否关联操作员
	 * @return
	 */
	public static boolean haveRelate(){
		String jkbxr = null;
		try {
			jkbxr = NCLocator.getInstance().lookup(IUserPubService.class)
				.queryPsndocByUserid(YerUtil.getPk_user());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(),e);
			throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0084")/*@res "查询用户关联人员报错!"*/);
		}
		return StringUtil.isEmpty(jkbxr);
	}
	
	/**
	 * 获取单据类型VO
	 * @param billtype
	 * @param group
	 * @return
	 * @throws BusinessException
	 */
	public static DjLXVO getDjlxvos(String billtype, String group) {
		DjLXVO[] vos;
		try {
			vos = new DjLXVO[] { ProxyDjlx.getIArapBillTypePublic().getDjlxvoByDjlxbm(billtype, group) };
			if(vos != null && vos.length != 0)
				return vos[0];
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0085")/*@res "查找单据类型VO出错!"*/);
		}

		return null;
	}
	
	public static String getPKGroup(){
		String pk_group = ((PtSessionBean)LfwRuntimeEnvironment.getLfwSessionBean()).getPk_unit();
		return pk_group;
	}
	
	public static boolean isNcUser() {
		CpUserVO cpUserVO = null;
		try {
			cpUserVO  = NCLocator.getInstance().lookup(ICpUserQry.class).getUserByPk(YerUtil.getPk_user());
		} catch (CpbBusinessException e) {
			Logger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e.getMessage());
		}
		if (cpUserVO!=null) {
			if ("NC".equals(cpUserVO.getOriginal())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取登陆用户对应的主组织  单据和帐表查询处用
	 * @return
	 */
	public static String getPK_ORG_Q() {
//		String org = getDefaultOrgUnit();
//		if (org==null || org.trim().length() == 0) {
//			String userID = getPk_user();
//			String[] str = getPsnDocInfo(userID);
//			org = str[2];
//		}
//
//		return org;
		

		String userID = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		try {
			String[] str = NCLocator.getInstance().lookup(IBXBillPrivate.class).queryPsnidAndDeptid(userID, YerUtil.getPK_group());
			if(str==null || nc.vo.jcom.lang.StringUtil.isEmpty(str[0])){
				//未设置关联业务员，返回
				return null;
			}
			return str[2];
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
	
	}
	
	/**
	 * 返回个性化中心设置的业务单元,没有设置，返回null
	 * @return
	 */
	public static String getDefaultOrgUnit() {
		try {
			return getSettingValue("org_df_biz");
		} catch (BusinessException e) {
			Logger.error(e);
		}
		return null;
	}
	
	/**
	 * 根据key返回个性化中心设置的value
	 * @param key
	 * @return
	 * @throws BusinessException
	 */
	private static String getSettingValue(String key) throws BusinessException{
		 return IndividuationManager.getIndividualSetting("nc.individuation.defaultData.DefaultConfigPage", false).getString(key);
	}
	
	/**
	 * 获取有功能权限的组织  portal端组织权限
	 * @param funcode
	 * @return
	 * @throws BusinessException
	 */
	public static String[] getPermissionOrgsPortal() throws CpbBusinessException{
		String userID = getPk_user();
		CpOrgVO[] orgvos = NCLocator.getInstance().lookup(ICpOrgQry.class).queryGroupsByAdminUser(userID);
		if(orgvos==null || orgvos.length==0)
			return new String[]{};

		String[] orgs=new String[orgvos.length];
		for (int i = 0; i < orgvos.length; i++) {
			orgs[i]=orgvos[i].getPk_org();
		}
		return orgs;
		
	}
	
	/**
	 * 获取有功能权限的组织 版本  portal端组织权限
	 * @param funcode
	 * @return
	 * @throws BusinessException
	 */
	public static String[] getPermissionOrgsPortalv() throws BusinessException{
		String userID = getPk_user();
		CpOrgVO[] orgvos = NCLocator.getInstance().lookup(ICpOrgQry.class).queryGroupsByAdminUser(userID);
		if(orgvos==null || orgvos.length==0)
			return new String[]{};

		String[] orgs=new String[orgvos.length];
		for (int i = 0; i < orgvos.length; i++) {
			orgs[i]=orgvos[i].getPk_org();
		}
		
		//目前协同组织表里没有版本信息，此处获得nc组织表中对应的版本信息
		OrgVO[] ncOrgVOs = NCLocator.getInstance().lookup(IOrgUnitQryService.class).getOrgs(orgs);
		
		String[] orgVids=new String[ncOrgVOs.length];
		for (int i = 0; i < ncOrgVOs.length; i++) {
			orgVids[i]=ncOrgVOs[i].getPk_vid();
		}
		
		return orgVids;
	}
	
	   /**
     * 取得当前语种对应的名称字段名
     * @param nameField 默认语种字段名
     * @return 当前语种对应的名称字段名
     */
    public static String getCurrentLangNameColumn(String nameField){
        return getLangNameColumn(nameField,getLangCode());
    }
    /**
     * 取得指定语种对应的名称字段名
     * @param nameField 默认语种字段名
     * @return 指定语种对应的名称字段名
     */
    public static String getLangNameColumn(String nameField,String langCode){
    	int seq =  BDLanguageHelper.getLangSeqBycode(langCode);
        return nameField + (seq > 1?seq:"");
    }
    
    /**
     * 取得当前语种编码
     * @return 当前语种编码
     */
    public static String getLangCode(){
    	try{
    		return LfwRuntimeEnvironment.getLangCode();
    	}
    	catch(Throwable e){
    		return InvocationInfoProxy.getInstance().getLangCode();
    	}
    }
    
	/**
	 * 获取单据类型
	 * @param funcode
	 * @return
	 */
	public static String getBillType(String funcode){

		String billtype;
		ICommonService commonService =  NCLocator.getInstance().lookup(ICommonService.class);
		try {
			billtype = commonService.getBillType(funcode);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0032")/*@res "查找单据类型出错!"*/);
		}

		return billtype;
	}
	
	/**
	 *公式1
	 */
	public static String getColValue(String table, String column, String pk,String pkValue) {
		FormulaParse parser = new FormulaParse();
		parser.setExpress("getColValue"+"(" + table + "," + column + "," + pk+ "," + "var"+")");
		parser.addVariable("var", pkValue);
		return parser.getValue();
	}


	/**
	 * 公式2
	 */
	public static String getColValue2(String table, String column, Object pk1,
			Object value1, Object pk2, Object value2) {
		FormulaParse parser = new FormulaParse();
		parser.setExpress("getColValue2"+"(" + table + "," + column + "," + pk1+ "," + "var1" + "," + pk2 + "," + "var2" + ")");
		parser.addVariable("var1", value1);
		parser.addVariable("var2", value2);
		return parser.getValue();
	}
	
	/**
	 * 修改ds的field方法
	 * @param ds
	 * @param whatField  修改编辑公式传"EditFormular"，修改精度传"Precision"
	 * @param fieldID
	 * @param value
	 */
	public static void modifyField(Dataset ds, String whatField, String fieldID, String value) {
		
		Field field = ds.getFieldSet().getField(fieldID);
		if ("Precision".equals(whatField) || whatField==null) {
			
			if (field instanceof UnmodifiableMdField) {
				Field newField = ((UnmodifiableMdField) field).getMDField();
				newField.setPrecision(value);
				ds.getFieldSet().updateField(newField.getId(), newField);
				
			} else {
				field.setPrecision(value);
			}
		}
		
		if ("EditFormular".equals(whatField)) {
			
			if (field instanceof UnmodifiableMdField) {
				
				Field newField = ((UnmodifiableMdField) field).getMDField();
				newField.setEditFormular(value);
				ds.getFieldSet().updateField(newField.getId(), newField);
				
				
			} else {
				field.setEditFormular(value);
			}
		}
		
		
	}
	
	/**
	 * 获取父页面Ds某字段的值
	 * @param name
	 * @return
	 */
	public static Object getParentDsValue(String name) {
		String mainWidgetId = "main";
		
		String parentPageID = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(/*WebContext.PARENT_PAGE_ID*/"$PARENT_PAGE_ID");
		LfwWindow parentPm = null;
		LfwView widget = null;
		if (parentPageID != null && !"".equals(parentPageID)) {  
			parentPm = AppLifeCycleContext.current().getApplicationContext().getWindowContext(parentPageID).getWindow();
			if (parentPm == null) {
				return null;
			}
			widget = parentPm.getWidget(mainWidgetId);
		} else { //匹配输入模式
			parentPm = AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().getWindow();
			if (parentPm == null) {
				return null;
			}
			widget = parentPm.getWidget(mainWidgetId);
		}
		
		String dataSetID = "";
		DatasetRelations dsRel = widget.getViewModels().getDsrelations();
		if (dsRel!= null) {
			DatasetRelation[] relationArr = dsRel.getDsRelations();
			if (relationArr != null && relationArr.length>0) {
				dataSetID = relationArr[0].getMasterDataset();
			}
		}
		
		Dataset parentDs = widget.getViewModels().getDataset(dataSetID);
		if (parentDs != null && parentDs.getSelectedRow()!=null && parentDs.nameToIndex(name)!=-1) {
			Object result = parentDs.getSelectedRow().getValue(parentDs.nameToIndex(name));
			return result;
		}
		return null;
	}
	
	/**
	 * 获取子页面Ds某字段的值
	 * @param name
	 * @return
	 */
	public static Object getDetailDsValue(String name) {
		String mainWidgetId = "main";
		
		String parentPageID = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(/*WebContext.PARENT_PAGE_ID*/"$PARENT_PAGE_ID");
		LfwWindow parentPm = null;
		LfwView widget = null;
		if (parentPageID != null && !"".equals(parentPageID)) {  
			parentPm = AppLifeCycleContext.current().getApplicationContext().getWindowContext(parentPageID).getWindow();
			if (parentPm == null) {
				return null;
			}
			widget = parentPm.getWidget(mainWidgetId);
		} else { //匹配输入模式
			parentPm = AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().getWindow();
			if (parentPm == null) {
				return null;
			}
			widget = parentPm.getWidget(mainWidgetId);
		}
		
		String dataSetID = "";
		DatasetRelations dsRel = widget.getViewModels().getDsrelations();
		if (dsRel!= null) {
			DatasetRelation[] relationArr = dsRel.getDsRelations();
			if (relationArr != null && relationArr.length>0) {
				dataSetID = relationArr[0].getDetailDataset();
			}
		}
		
		Dataset detailDs = widget.getViewModels().getDataset(dataSetID);
		if (detailDs != null && detailDs.getSelectedRow()!=null && detailDs.nameToIndex(name)!=-1) {
			Object result = detailDs.getSelectedRow().getValue(detailDs.nameToIndex(name));
			return result;
		}
		return null;
	}
	
	public static void setFormEleUnEdit(String formID,String[] eleNames) {
		ViewContext viewCtx = AppLifeCycleContext.current().getWindowContext().getViewContext("main");
		LfwView widget = viewCtx.getView();
		GridComp form = (GridComp)widget.getViewComponents().getComponent(formID);
		if (form != null) {
			for (String eleName: eleNames ){
				GridColumn ele = form.getElementById(eleName);
				if (ele != null){
					ele.setEditable(false);  //设不可编辑用此
				}
			}
		}
	}
	
	

	
	public static void setFormEleEdit(String formID,String[] eleNames) {
		ViewContext viewCtx = AppLifeCycleContext.current().getWindowContext().getViewContext("main");
		LfwView widget = viewCtx.getView();
		GridComp form = (GridComp)widget.getViewComponents().getComponent(formID);
		if (form != null) {
			for (String eleName: eleNames ){
				GridColumn ele = form.getElementById(eleName);
				if (ele != null){
					ele.setEditable(true);  //设可编辑用此
				}
			}
		}
	}
	
	/**
	 * 表单和子表的编辑状态
	 * @param mainWidget
	 * @param isFormStateEnabled
	 * @param isGridMenuEnabled
	 * @param isEnabled
	 */
	public static void setFormState(LfwView mainWidget, boolean isFormStateEnabled,
			boolean isGridMenuEnabled, boolean isEnabled) {

		WebComponent[] components = mainWidget.getViewComponents()
				.getComponents();

		if (components != null) {

			for (WebComponent com : components) {
				if (com instanceof FormComp) {
					if (isFormStateEnabled) {
						((FormComp) com).setRenderType(5);
					} else {
						((FormComp) com).setRenderType(6);
					}
				}

				if (com instanceof GridComp) {
					if (!isGridMenuEnabled) {
						MenubarComp menuBar = ((GridComp) com).getMenuBar();
						if (menuBar != null){
							
							List<MenuItem> menuList = menuBar.getMenuList();
							
							for (MenuItem menu : menuList) {
								menu.setEnabled(false);
							}
						}
						
						String dsId = ((GridComp) com).getDataset();
						
						Dataset gridDs = mainWidget.getViewModels().getDataset(dsId);
							
						if(gridDs != null){
							gridDs.setEnabled(false);
						}
						
					}
				}
			}
		}
		
		
		String[] masterDsIds = mainWidget.getViewModels().getDsrelations().getMasterDatasetIds();
		
		if(masterDsIds != null && masterDsIds.length > 0){
			
			Dataset masterDs = mainWidget.getViewModels().getDataset(masterDsIds[0]);
			
			if(masterDs != null){
				
				if(!isEnabled){
					masterDs.setEnabled(false);
				}else{
					masterDs.setEnabled(true);
				}
			}
		}
	}
	
	public static String getMasterDataset(LfwView widget) {
		if (null != widget.getViewModels().getDsrelations()) {
			DatasetRelation[] relationList = widget.getViewModels().getDsrelations().getDsRelations();
			if (relationList.length > 0) {
				DatasetRelation relation = relationList[0];
				String masterDataset = relation.getMasterDataset();
				return masterDataset;
			}
		}
		Dataset[] dss = widget.getViewModels().getDatasets();
		for (int i = 0; i < dss.length; i++) {
			if(dss[i] instanceof IRefDataset)
				continue;
			return dss[i].getId();
		}
		return "";
	}
	
	public static Dataset[] getDetailDatasets(LfwView widget, String masterDsId) {
		List detailDsList = new ArrayList<Dataset>();
		DatasetRelation[] masterRels = widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
		for (int i = 0; i < masterRels.length; i++) {
			//获取子对应的外键值，并设置到VO条件中
			DatasetRelation dr = masterRels[i];
			Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
			detailDsList.add(detailDs);
		}
		return (Dataset[]) detailDsList.toArray(new Dataset[0]);

	}
	
	public static String getAggvo(String fullClassName){
		
		IDatasetProvider dataProvider = NCLocator.getInstance().lookup(IDatasetProvider.class);
		try {
			return dataProvider.getAggVo(fullClassName);
		} catch (LfwBusinessException e) {
			// TODO Auto-generated catch block
			LfwLogger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 对Row赋值
	 * @param row
	 * @param ds
	 * @param name
	 * @param value
	 */
	public static void setRowValue(Row row, Dataset ds, String name, Object value) {
		if (ds.nameToIndex(name) != -1) {
			row.setValue(ds.nameToIndex(name), value);
		}
	}
	
	public static Object getRowValue(Row row, Dataset ds, String name) {
		if (ds.nameToIndex(name) != -1) {
			 return row.getValue(ds.nameToIndex(name));
		}else{
			return null;
		}
	}
}
