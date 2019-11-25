package nc.ui.erm.billpub.action;

import java.awt.event.ActionEvent;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.er.util.BXUiUtil;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.pub.bill.BillItem;
import nc.ui.uif2.actions.AddAction;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uif2.LoginContext;

public class ErmBillAddAction extends AddAction {
	private static final long serialVersionUID = 1L;
	boolean isFirstOnAdd = true;
	private ErmBillBillForm editor;

	public void doAction(ActionEvent e) throws Exception {
		//获取交易类型
		String selectBillTypeCode = ((ErmBillBillManageModel) getModel()).getSelectBillTypeCode();


		check();
		super.doAction(e);

		
		if(ServiceFeeTaxUtils.TRANSTYPE.equals(selectBillTypeCode)){
			// 应发金额
			BillItem amountyingfa = editor.getBillCardPanel().getBodyItem("amount");
			// 实发金额
			BillItem amountshifa = editor.getBillCardPanel().getBodyItem("defitem44");
			// 本次预计扣税：defitem46
			BillItem defitem46 = editor.getBillCardPanel().getBodyItem("defitem46");
			// 增补扣税：defitem45
			BillItem defitem45 = editor.getBillCardPanel().getBodyItem("defitem45");
			// 累计应发 defitem47
			BillItem defitem47 = editor.getBillCardPanel().getBodyItem("defitem47");
			
			amountyingfa.setEnabled(false);
			amountshifa.setEnabled(false);
			defitem46.setEnabled(false);
			defitem45.setEnabled(false);
			defitem47.setEnabled(false);
		}
	}

	public void check() throws BusinessException {
		String pkPsn = BXUiUtil.getPk_psndoc();
		if (pkPsn == null) {
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2011ermpub0316_0", "02011ermpub0316-0000"));
		}

		String selectBillTypeCode = ((ErmBillBillManageModel) getModel())
				.getSelectBillTypeCode();
		if (((ErmBillBillManageModel) getModel())
				.getCurrentDjlx(selectBillTypeCode).getFcbz().booleanValue()) {
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2011", "UPP2011-000171"));
		}

		String nodeCode = getModel().getContext().getNodeCode();
		if ((!nodeCode.equals("20110BO")) && (!nodeCode.equals("20110CBSG"))
				&& (!nodeCode.equals("20110CBS"))
				&& (!nodeCode.equals("20110RB"))) {
			UFBoolean isMactrl = ((ErmBillBillManageModel) getModel())
					.getCurrentDjlx(selectBillTypeCode).getIs_mactrl();
			if ((isMactrl != null) && (isMactrl.booleanValue()))
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("2011", "UPP2011-000925"));
		}
	}

	public Object getCacheValue(String key) {
		return WorkbenchEnvironment.getInstance().getClientCache(key);
	}

	public boolean isFirstOnAdd() {
		return this.isFirstOnAdd;
	}

	public void setFirstOnAdd(boolean isFirstOnAdd) {
		this.isFirstOnAdd = isFirstOnAdd;
	}

	public ErmBillBillForm getEditor() {
		return this.editor;
	}

	public void setEditor(ErmBillBillForm editor) {
		this.editor = editor;
	}
}