package nc.ui.bd.ref.model;

import nc.bs.logging.Logger;
import nc.individuation.defaultData.DefaultConfigPage;
import nc.individuation.property.pub.IndividualSetting;
import nc.individuation.property.pub.IndividuationManager;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.IRefDocEdit;
import nc.ui.bd.ref.IRefMaintenanceHandler;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;

public class VoucherTypeDefaultRefModel_HBGD extends AbstractRefModel {
	public VoucherTypeDefaultRefModel_HBGD() {
		reset();
	}

	public void reset() {
		setFieldCode(new String[] { "code", "name" });
		setFieldName(new String[] {
				NCLangRes4VoTransl.getNCLangRes().getStrByID("10140vtb",
						"2VCTYPE-000004"),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("10140vtb",
						"2VCTYPE-000001") });
		setHiddenFieldCode(new String[] { "pk_vouchertype" });
		setPkFieldCode("pk_vouchertype");
		setRefCodeField("code");
		setRefNameField("name");
		setTableName("bd_vouchertype");
		setResourceID("vouchertype");
		setAddEnableStateWherePart(true);
		setFilterRefNodeName(new String[] { "财务核算账簿(财务组织)" });

		setRefMaintenanceHandler(new IRefMaintenanceHandler() {
			public String[] getFucCodes() {
				return new String[] { "10140VTB", "10140VTG", "10140VTO" };
			}

			public IRefDocEdit getRefDocEdit() {
				return null;
			}
		});
		resetFieldName();
	}

	public void filterValueChanged(ValueChangedEvent changedValue) {
		super.filterValueChanged(changedValue);
		String[] selectedPKs = (String[]) changedValue.getNewValue();
		if ((selectedPKs != null) && (selectedPKs.length > 0))
			setPk_org(selectedPKs[0]);
	}

	protected String getEnvWherePart() {
		return "pk_org='" + getPk_group() + "' or " + "pk_org" + "='"
				+ "1001N610000000001OIU" + "' or " + "pk_org" + "='"
				+ getPk_org2() + "'";
	}

	private String getPk_org2() {
		String fa_pk = null;
		IndividualSetting indivSettings = null;
		try {
			indivSettings = IndividuationManager.getIndividualSetting(
					new DefaultConfigPage().getClass(), false);
			String pk = indivSettings.getString("df_voucher_type");

			fa_pk = indivSettings.getString("org_df_fa");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

		return fa_pk;
	}
}