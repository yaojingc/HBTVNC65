package nc.individuation.defaultData;

import java.awt.Dimension;
import java.io.Serializable;
import javax.swing.JComponent;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.individuation.property.pub.IndividualSetting;
import nc.pubitf.setting.defaultdata.IDefaultSettingConst;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.profile.FunctionPermProfileManager;
import nc.vo.uap.rbac.profile.IFunctionPermProfile;
import net.miginfocom.swing.MigLayout;

public class BusinessOrgConfigPanel extends BaseOrgConfigPanel implements
		IDefaultSettingConst, Serializable {
	private static final long serialVersionUID = -7774266851489099894L;
	private UIRefPane tfFa = null;

	private UIRefPane tfCost = null;

	private UIRefPane tfBiz = null;

	private UIRefPane tfResp = null;

	private UIRefPane tfCredit = null;

	private UIRefPane tfConent = null;

	private UIRefPane tfTax = null;

	private UIRefPane tfTaxbook = null;

	private UIRefPane tfcenterContent = null;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private UIRefPane tfmaterial = null;
	
	
	// 添加一个默认凭证类别bd_vouchertype
	private UIRefPane tfvouchertype = null;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public BusinessOrgConfigPanel() {
		initUI();
	}

	private void initUI() {
		MigLayout layout = new MigLayout("", "[]rel[]unrel[]rel[]",
				"[]20[]20[]20[]");
		setLayout(layout);

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//		this.tfmaterial = new UIRefPane();
//		this.tfmaterial.setRefNodeName("物料1");
//		setRefPanelPreferredSize(this.tfmaterial);
		
		this.tfvouchertype = new UIRefPane();
		this.tfvouchertype.setRefNodeName("默认凭证类别");
		setRefPanelPreferredSize(this.tfvouchertype);
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		this.tfFa = new UIRefPane();
		this.tfFa.setRefNodeName("财务核算账簿");
		setRefPanelPreferredSize(this.tfFa);

		this.tfCost = new UIRefPane();
		this.tfCost.setRefNodeName("成本域");
		setRefPanelPreferredSize(this.tfCost);

		this.tfBiz = new UIRefPane();
		this.tfBiz.setRefNodeName("业务单元");
		setRefPanelPreferredSize(this.tfBiz);

		this.tfResp = new UIRefPane();
		this.tfResp.setRefNodeName("责任核算账簿");
		setRefPanelPreferredSize(this.tfResp);

		this.tfCredit = new UIRefPane();
		this.tfCredit.setRefNodeName("信用控制域");
		setRefPanelPreferredSize(this.tfCredit);

		this.tfConent = new UIRefPane();
		this.tfConent.setRefNodeName("合并主体");
		setRefPanelPreferredSize(this.tfConent);

		this.tfTax = new UIRefPane();
		this.tfTax.setRefNodeName("税务组织");
		setRefPanelPreferredSize(this.tfTax);
		this.tfTaxbook = new UIRefPane();
		this.tfTaxbook.setRefNodeName("税务核算账簿");
		setRefPanelPreferredSize(this.tfTaxbook);
		this.tfTaxbook.setWhereString(" taxenablestate = '2'");

		this.tfcenterContent = new UIRefPane();
		this.tfcenterContent.setRefNodeName("利润中心成本域");
		setRefPanelPreferredSize(this.tfcenterContent);

		checkPerm();

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//		UILabel material = new UILabel(NCLangRes.getInstance().getStrByID(
//				"sfapp", "OrgConfigPanel-000002"));
//		setLabelPreferredSize(material);
//		add(material);
//		add(this.tfmaterial);
		
		UILabel vouchertype = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000002"));
		setLabelPreferredSize(vouchertype);
		add(vouchertype);
		add(this.tfvouchertype);
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		UILabel buzUnitLbl = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000002"));
		setLabelPreferredSize(buzUnitLbl);
		add(buzUnitLbl);
		add(this.tfBiz);

		UILabel faLbl = new UILabel(NCLangRes.getInstance().getStrByID("sfapp",
				"OrgConfigPanel-000000"));
		setLabelPreferredSize(faLbl);
		add(faLbl);
		add(this.tfFa, "wrap");

		UILabel respLabel = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000003"));
		setLabelPreferredSize(respLabel);
		add(respLabel);
		add(this.tfResp);

		UILabel costLbl = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000001"));
		setLabelPreferredSize(costLbl);
		add(costLbl);
		add(this.tfCost, "wrap");

		UILabel creditLbl = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000004"));
		setLabelPreferredSize(creditLbl);
		add(creditLbl);
		add(this.tfCredit);

		UILabel conentLbl = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000005"));
		setLabelPreferredSize(conentLbl);
		add(conentLbl);
		add(this.tfConent, "wrap");

		UILabel taxLbl = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000006"));
		setLabelPreferredSize(taxLbl);
		add(taxLbl);
		add(this.tfTax);

		UILabel taxbookLbl = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000007"));
		setLabelPreferredSize(taxbookLbl);
		add(taxbookLbl);
		add(this.tfTaxbook, "wrap");

		UILabel centerLbl = new UILabel(NCLangRes.getInstance().getStrByID(
				"sfapp", "OrgConfigPanel-000008"));
		setLabelPreferredSize(taxbookLbl);
		add(centerLbl);
		add(this.tfcenterContent);
	}

	private void setLabelPreferredSize(JComponent comp) {
		comp.setPreferredSize(new Dimension(127, 20));
	}

	private void setRefPanelPreferredSize(JComponent comp) {
		comp.setPreferredSize(new Dimension(127, 20));
	}
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//	public String getMaterial() {
//		return this.tfFa.getRefPK();
//	}
//
//	public void setMaterial(String fa) {
//		this.tfFa.setPK(fa);
//	}
	
	public String getvouchertype() {
		return this.tfvouchertype.getRefPK();
	}

	public void setvouchertype(String fa) {
		this.tfvouchertype.setPK(fa);
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	

	public String getFa() {
		return this.tfFa.getRefPK();
	}

	public void setFa(String fa) {
		this.tfFa.setPK(fa);
	}

	public String getCost() {
		return this.tfCost.getRefPK();
	}

	public void setCost(String cost) {
		this.tfCost.setPK(cost);
	}

	public String getBiz() {
		return this.tfBiz.getRefPK();
	}

	public void setBiz(String biz) {
		this.tfBiz.setPK(biz);
	}

	public String getResp() {
		return this.tfResp.getRefPK();
	}

	public void setResp(String resp) {
		this.tfResp.setPK(resp);
	}

	public String getCredit() {
		return this.tfCredit.getRefPK();
	}

	public void setCredit(String credt) {
		this.tfCredit.setPK(credt);
	}

	public String getConent() {
		return this.tfConent.getRefPK();
	}

	public void setConent(String conent) {
		this.tfConent.setPK(conent);
	}

	public String getTax() {
		return this.tfTax.getRefPK();
	}

	public void setTax(String tax) {
		this.tfTax.setPK(tax);
	}

	public String getTaxbook() {
		return this.tfTaxbook.getRefPK();
	}

	public void setTaxbook(String taxbook) {
		this.tfTaxbook.setPK(taxbook);
	}

	public String getCenterContent() {
		return this.tfcenterContent.getRefPK();
	}

	public void setCenterContent(String taxbook) {
		this.tfcenterContent.setPK(taxbook);
	}

	private void checkPerm() {
		UserVO currentUserVO = WorkbenchEnvironment.getInstance()
				.getLoginUser();

		IFunctionPermProfile profile = FunctionPermProfileManager.getInstance()
				.getProfile(currentUserVO.getUser_code());

		if (profile != null) {
			String[] orgIDs = profile.getPermPkorgs();
			if (orgIDs == null) {
				return;
			}
			
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			this.tfvouchertype.getRefModel().setFilterPks(orgIDs);
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			
			
			this.tfBiz.getRefModel().setFilterPks(orgIDs);
			this.tfFa.getRefModel().setFilterPks(orgIDs);
			this.tfTaxbook.getRefModel().setFilterPks(orgIDs);
			this.tfResp.getRefModel().setFilterPks(orgIDs);
			this.tfTax.getRefModel().setFilterPks(orgIDs);
		}
	}

	public void settingsToView(IndividualSetting indivSettings) {
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//		setMaterial(indivSettings.getString("bd_material"));
		
		
		setvouchertype(indivSettings.getString("bd_vouchertype"));
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		

		setBiz(indivSettings.getString("org_df_biz"));
		setFa(indivSettings.getString("org_df_fa"));
		setCost(indivSettings.getString("org_df_cost"));
		setResp(indivSettings.getString("org_df_resp"));
		setCredit(indivSettings.getString("org_df_credit"));
		setConent(indivSettings.getString("org_df_conent"));
		setTax(indivSettings.getString("org_df_tax"));
		setTaxbook(indivSettings.getString("org_df_taxbook"));
		setCenterContent(indivSettings.getString("org_df_centerContent"));
	}

	public void viewToSettings(IndividualSetting indivSettings) {
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//		indivSettings.putString("bd_material", getMaterial());
		indivSettings.putString("bd_vouchertype", getvouchertype());
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		
		indivSettings.putString("org_df_biz", getBiz());
		indivSettings.putString("org_df_fa", getFa());
		indivSettings.putString("org_df_cost", getCost());
		indivSettings.putString("org_df_resp", getResp());
		indivSettings.putString("org_df_credit", getCredit());
		indivSettings.putString("org_df_conent", getConent());
		indivSettings.putString("org_df_tax", getTax());
		indivSettings.putString("org_df_taxbook", getTaxbook());
		indivSettings.putString("org_df_centerContent", getCenterContent());
	}
}