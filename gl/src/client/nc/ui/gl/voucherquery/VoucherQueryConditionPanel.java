package nc.ui.gl.voucherquery;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.table.TableCellEditor;
import nc.bs.logging.Logger;
import nc.individuation.defaultData.DefaultConfigPage;
import nc.individuation.property.pub.IndividualSetting;
import nc.individuation.property.pub.IndividuationManager;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.gl.cachefeed.CacheRequestFactory;
import nc.ui.gl.datacache.GLParaDataCache;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.gl.gateway60.refmodel.RefUtilGL;
import nc.ui.gl.glref.GLBUVersionWithBookRefModel;
import nc.ui.gl.glref.GlAccountingBookRefModel;
import nc.ui.gl.remotecall.GlRemoteCallProxy;
import nc.ui.gl.view.LayoutPanel;
import nc.ui.gl.vouchermodels.OperatorDefaultRefModel;
import nc.ui.gl.vouchertools.VoucherDataCenter;
import nc.ui.glcom.ass.FreeValueList;
import nc.ui.glcom.control.SystemComboBox;
import nc.ui.glcom.control.YearPeriodDatePane;
import nc.ui.glcom.period.GlPeriodForClient;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.vo.fipub.utils.uif2.FiUif2MsgUtil;
import nc.vo.gateway60.accountbook.AccountBookUtil;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.gl.vouchertools.DapsystemVO;
import nc.vo.gl.vouchertools.QueryElementVO;
import nc.vo.glcom.ass.AssVO;
import nc.vo.glcom.glperiod.GlPeriodVO;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.Language;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.SetOfBookVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.funcreg.ModuleVO;
import org.apache.commons.lang.StringUtils;


// 查询
public class VoucherQueryConditionPanel extends UIPanel {
	private static final long serialVersionUID = 6372253856927710387L;
	private String modulecode;
	private UICheckBox ivjCkbErrorVoucher = null;

	private UIRefPane ivjRefCasher = null;

	private UIRefPane ivjRefTally = null;

	private UICheckBox ivjCkbAbandonVoucher = null;

	private UICheckBox ivjCkbTempVoucher = null;

	private UICheckBox ivjCkbNormalVoucher = null;

	private UIComboBox ivjCmbVoucherState = null;

	private UIRefPane ivjCmbVouchertype = null;
	private UILabel ivjLCasher = null;

	private UILabel ivjLChecked = null;

	private UILabel ivjLCorp = null;

	private UILabel ivjLPrepared = null;

	private UILabel ivjLSystem = null;

	private UILabel ivjLTally = null;

	private UILabel ivjLTo = null;

	private UILabel ivjLVoucherno = null;

	private UILabel ivjLVoucherState = null;

	private UILabel ivjLVouchertype = null;

	private UIRefPane ivjRefAbandon = null;

	private UIRefPane ivjRefChecked = null;

	private UIRefPane ivjRefPrepared = null;

	private SystemComboBox ivjRefSystem = null;

	private UITextField ivjTVouchernofrom = null;

	private UITextField ivjTVouchernoto = null;

	private LayoutPanel ivjUIPanel1 = null;

	private YearPeriodDatePane ivjYearPeriodDatePane1 = null;

	private UIRefPane ijRefCorp = null;

	private UIRefPane ivjRefCorp = null;

	private UILabel ivjLAttachment = null;

	private UILabel ivjLTo1 = null;

	private UITextField ivjTBeginAttachment = null;

	private UITextField ivjTEndAttachment = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private String m_selected_pk_corp;
	private FreeValueList ivjFreeValueList1 = null;

	private SelfDefQueryPanel ivjSelfDefQueryPanel1 = null;

	private UIPanel ivjUIPanel2 = null;

	private UIPanel ivjUIPanel4 = null;

	private UIPanel ivjUIPanel5 = null;

	private UIPanel ivjUIPanel6 = null;

	private UIScrollPane ivjUIScrollPane1 = null;

	private UIComboBox m_convertComboBox = null;

	private UILabel UILabel1 = null;

	private UILabel UILabel2 = null;

	private UIComboBox m_convertComboBoxs = null;

	private UIComboBox CmBDifflag = null;

	private UILabel UILabel = null;
	private String m_pk_glorgbook;
	private UILabel label;
	private UIRefPane buRefPane;

	public VoucherQueryConditionPanel() {
		initialize();
	}

	public VoucherQueryConditionPanel(String defaultporg) {
		this.m_pk_glorgbook = defaultporg;
		initialize();
	}

	private void connEtoC1(ValueChangedEvent arg1) {
		try {
			refCorp_ValueChanged(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC2(ValueChangedEvent arg1) {
		try {
			refBU_ValueChanged(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC4(ValueChangedEvent event) {
		UFDate newDate = null;
		if ((event.getNewValue() instanceof UFDate))
			newDate = (UFDate) event.getNewValue();
		else if ((event.getNewValue() instanceof String)) {
			newDate = new UFDate(event.getNewValue().toString());
		}
		((GLBUVersionWithBookRefModel) getBURefPane().getRefModel())
				.setVstartdate(newDate.asEnd());
	}

	private void connEtoC3(ValueChangedEvent event) {
	}

	private UICheckBox getCkbAbandonVoucher() {
		if (this.ivjCkbAbandonVoucher == null) {
			try {
				this.ivjCkbAbandonVoucher = new UICheckBox();
				this.ivjCkbAbandonVoucher.setName("CkbAbandonVoucher");
				this.ivjCkbAbandonVoucher.setText(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000275"));

				this.ivjCkbAbandonVoucher.setBounds(193, 183, 80, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCkbAbandonVoucher;
	}

	private UICheckBox getCkbTempVoucher() {
		if (this.ivjCkbTempVoucher == null) {
			try {
				this.ivjCkbTempVoucher = new UICheckBox();
				this.ivjCkbTempVoucher.setName("CkbTempVoucher");
				this.ivjCkbTempVoucher.setText(NCLangRes4VoTransl
						.getNCLangRes().getStrByID("glpub_0", "02002003-0254"));
				this.ivjCkbTempVoucher.setBounds(283, 183, 80, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCkbTempVoucher;
	}

	private UICheckBox getCkbErrorVoucher() {
		if (this.ivjCkbErrorVoucher == null) {
			try {
				this.ivjCkbErrorVoucher = new UICheckBox();
				this.ivjCkbErrorVoucher.setName("CkbErrorVoucher");
				this.ivjCkbErrorVoucher.setText(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000276"));

				this.ivjCkbErrorVoucher.setBounds(100, 183, 80, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCkbErrorVoucher;
	}

	private UICheckBox getCkbNormalVoucher() {
		if (this.ivjCkbNormalVoucher == null) {
			try {
				this.ivjCkbNormalVoucher = new UICheckBox();
				this.ivjCkbNormalVoucher.setName("CkbNormalVoucher");
				this.ivjCkbNormalVoucher.setText(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000277"));

				this.ivjCkbNormalVoucher.setBounds(8, 183, 80, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCkbNormalVoucher;
	}

	private UIComboBox getCmbVoucherState() {
		if (this.ivjCmbVoucherState == null) {
			try {
				this.ivjCmbVoucherState = new UIComboBox();
				this.ivjCmbVoucherState.setName("CmbVoucherState");
				this.ivjCmbVoucherState.setLocation(420, 35);

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000278"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000279"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000280"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000281"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000282"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000283"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000284"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000285"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000286"));

				this.ivjCmbVoucherState.addItem(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000287"));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCmbVoucherState;
	}

	private UIRefPane getCmbVouchertype() {
		if (this.ivjCmbVouchertype == null) {
			try {
				this.ivjCmbVouchertype = RefUtilGL.newRawVoucherTypeRefpane();

				this.ivjCmbVouchertype.setBounds(70, 45, 100, 22);
				this.ivjCmbVouchertype.setDisabledDataButtonShow(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCmbVouchertype;
	}

	public QueryElementVO[] getConditionVOs() {
		Vector t = new Vector();
		if (getRefCorp().getRefPK() != null) {
			if (getRefCorp().getRefPKs().length == 1) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_accountingbook");
				tempvo.setDatas(new String[] { getRefCorp().getRefPK() });
				t.addElement(tempvo);
			} else {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_accountingbook");
				tempvo.setDatas(getRefCorp().getRefPKs());
				t.addElement(tempvo);
			}
		} else {
			String pk_user = GlWorkBench.getLoginUser();
			String[] pk_orgbooks = (String[]) null;

			Vector vecorgbookpks = new Vector();
			if (pk_orgbooks != null) {
				String pk_book = AccountBookUtil.getGLBookVOByPk_GlOrgBook(
						VoucherDataCenter.getClientPk_orgbook())
						.getPk_setofbook();
				for (int i = 0; i < pk_orgbooks.length; i++) {
					if (pk_orgbooks[i] == null)
						continue;
					if (!pk_book.equals(AccountBookUtil
							.getGLBookVOByPk_GlOrgBook(pk_orgbooks[i])
							.getPk_setofbook()))
						continue;
					vecorgbookpks.addElement(pk_orgbooks[i]);
				}
			}
			String[] pks = (String[]) null;
			if (vecorgbookpks.size() > 0) {
				pks = new String[vecorgbookpks.size()];
				vecorgbookpks.copyInto(pks);
			}
			if ((pks == null) || (pks.length == 0)) {
				FiUif2MsgUtil.showUif2DetailMessage(
						this,
						NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
								"02002003-0102"),
						NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
								"02002003-0193"));
				return null;
			}
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.pk_accountingbook");
			tempvo.setDatas(pks);
			t.addElement(tempvo);
		}

		if (getCmbVouchertype().getRefPK() != null) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.pk_vouchertype");
			tempvo.setDatas(new String[] { getCmbVouchertype().getRefPK() });
			t.addElement(tempvo);
		} else if (getRefCorp().getRefPK() == null) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.pk_vouchertype");
		} else {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.pk_vouchertype");
		}

		if (getYearPeriodDatePane1().getSelectionState() == 0) {
			if (getYearPeriodDatePane1().getBeginyear().equals(
					getYearPeriodDatePane1().getEndyear()))
				if (!getYearPeriodDatePane1().getBeginperiod().equals(
						getYearPeriodDatePane1().getEndperiod())) {
					QueryElementVO tempvo = new QueryElementVO();
					tempvo.setOperator("(");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setOperator("(");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.year");
					tempvo.setDatas(new String[] { getYearPeriodDatePane1()
							.getBeginyear() });
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");

					tempvo.setOperator(">=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.adjustperiod");
					tempvo.setDatas(new String[] { getYearPeriodDatePane1()
							.getBeginperiod() });
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setOperator(")");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator(">");
					tempvo.setIsAnd(UFBoolean.FALSE);
					tempvo.setRestrictfield("gl_voucher.year");
					tempvo.setDatas(new String[] { getYearPeriodDatePane1()
							.getBeginyear() });

					tempvo = new QueryElementVO();
					tempvo.setOperator(")");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setOperator("(");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setOperator("(");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.year");
					tempvo.setDatas(new String[] { getYearPeriodDatePane1()
							.getEndyear() });

					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("<=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.adjustperiod");
					tempvo.setDatas(new String[] { getYearPeriodDatePane1()
							.getEndperiod() });
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setOperator(")");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("<");
					tempvo.setIsAnd(UFBoolean.FALSE);
					tempvo.setRestrictfield("gl_voucher.year");
					tempvo.setDatas(new String[] { getYearPeriodDatePane1()
							.getEndyear() });

					tempvo = new QueryElementVO();
					tempvo.setOperator(")");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
				}
			if (getYearPeriodDatePane1().getBeginyear().equals(
					getYearPeriodDatePane1().getEndyear()))
				if (getYearPeriodDatePane1().getBeginperiod().equals(
						getYearPeriodDatePane1().getEndperiod())) {
					QueryElementVO tempvo = new QueryElementVO();
					tempvo.setOperator("(");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.year");
					tempvo.setDatas(new String[] { getYearPeriodDatePane1()
							.getBeginyear() });
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.adjustperiod");
					tempvo.setDatas(new String[] { getYearPeriodDatePane1()
							.getEndperiod() });
					t.addElement(tempvo);
					tempvo = new QueryElementVO();
					tempvo.setOperator(")");
					tempvo.setIsAnd(UFBoolean.TRUE);
					t.addElement(tempvo);
				}
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setOperator("(");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setOperator("(");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.year");
			tempvo.setDatas(new String[] { getYearPeriodDatePane1()
					.getBeginyear() });
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator(">=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.adjustperiod");
			tempvo.setDatas(new String[] { getYearPeriodDatePane1()
					.getBeginperiod() });
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setOperator(")");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator(">");
			tempvo.setIsAnd(UFBoolean.FALSE);
			tempvo.setRestrictfield("gl_voucher.year");
			tempvo.setDatas(new String[] { getYearPeriodDatePane1()
					.getBeginyear() });
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setOperator(")");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setOperator("(");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setOperator("(");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.year");
			tempvo.setDatas(new String[] { getYearPeriodDatePane1()
					.getEndyear() });
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("<=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.adjustperiod");
			tempvo.setDatas(new String[] { getYearPeriodDatePane1()
					.getEndperiod() });
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setOperator(")");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("<");
			tempvo.setIsAnd(UFBoolean.FALSE);
			tempvo.setRestrictfield("gl_voucher.year");
			tempvo.setDatas(new String[] { getYearPeriodDatePane1()
					.getEndyear() });
			t.addElement(tempvo);
			tempvo = new QueryElementVO();
			tempvo.setOperator(")");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);
		} else {
			if (!getYearPeriodDatePane1().getBegindate().toString().equals("")) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator(">=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.prepareddate");
				tempvo.setDatas(new String[] { getYearPeriodDatePane1()
						.getBegindate().toString() });
				t.addElement(tempvo);
			}
			if (!getYearPeriodDatePane1().getEnddate().toString().equals("")) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("<=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.prepareddate");
				tempvo.setDatas(new String[] { getYearPeriodDatePane1()
						.getEnddate().toString() });
				t.addElement(tempvo);
			}
		}
		label2074: if (!getTVouchernofrom().getText().trim().equals("")) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("int");
			tempvo.setOperator(">=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.num");
			tempvo.setDatas(new String[] { getTVouchernofrom().getValue()
					.toString() });
			t.addElement(tempvo);
		}
		if (!getTVouchernoto().getText().trim().equals("")) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("int");
			tempvo.setOperator("<=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.num");
			tempvo.setDatas(new String[] { getTVouchernoto().getValue()
					.toString() });
			t.addElement(tempvo);
		}
		if (!getTBeginAttachment().getText().trim().equals("")) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("int");
			tempvo.setOperator(">=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.attachment");
			tempvo.setDatas(new String[] { getTBeginAttachment().getValue()
					.toString() });
			t.addElement(tempvo);
		}
		if (!getTEndAttachment().getText().trim().equals("")) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("int");
			tempvo.setOperator("<=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.attachment");
			tempvo.setDatas(new String[] { getTEndAttachment().getValue()
					.toString() });
			t.addElement(tempvo);
		}
		if (!getRefSystem().getSelectedItem().equals(
				NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000288"))) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			StringBuffer sqlBuff = new StringBuffer();
			sqlBuff.append("RTRIM(gl_voucher.pk_system) = '"
					+ getRefSystem().getSelectedPk() + "' ");
			sqlBuff.append(" or ");
			sqlBuff.append(" RTRIM(gl_voucher.pk_system) LIKE '"
					+ getRefSystem().getSelectedPk() + ",%' ");
			sqlBuff.append(" or ");
			sqlBuff.append(" RTRIM(gl_voucher.pk_system) LIKE '%,"
					+ getRefSystem().getSelectedPk() + ",%' ");
			sqlBuff.append(" or ");
			sqlBuff.append(" RTRIM(gl_voucher.pk_system) ");
			tempvo.setRestrictfield(sqlBuff.toString());
			tempvo.setDatas(new String[] { "%,"
					+ getRefSystem().getSelectedPk() });
			t.addElement(tempvo);
		}
		if ((getRefPrepared().getRefPK() != null)
				&& (getRefPrepared().getRefPK().length() > 0)) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.pk_prepared");
			tempvo.setDatas(new String[] { getRefPrepared().getRefPK() });
			t.addElement(tempvo);
		}

		if ((getRefCasher().getRefPK() != null)
				&& (getRefCasher().getRefPK().length() > 0)) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.pk_casher");
			tempvo.setDatas(new String[] { getRefCasher().getRefPK() });
			t.addElement(tempvo);
		}
		if ((getRefChecked().getRefPK() != null)
				&& (getRefChecked().getRefPK().length() > 0)) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.pk_checked");
			tempvo.setDatas(new String[] { getRefChecked().getRefPK() });
			t.addElement(tempvo);
		}
		if ((getRefTally().getRefPK() != null)
				&& (getRefTally().getRefPK().length() > 0)) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setDatatype("String");
			tempvo.setOperator("=");
			tempvo.setIsAnd(UFBoolean.TRUE);
			tempvo.setRestrictfield("gl_voucher.pk_manager");
			tempvo.setDatas(new String[] { getRefTally().getRefPK() });
			t.addElement(tempvo);
		}

		if ((getCkbNormalVoucher().isSelected())
				|| (getCkbErrorVoucher().isSelected())
				|| (getCkbAbandonVoucher().isSelected())
				|| (getCkbTempVoucher().isSelected())) {
			QueryElementVO tempvo = new QueryElementVO();
			tempvo.setOperator("(");
			tempvo.setIsAnd(UFBoolean.TRUE);
			t.addElement(tempvo);

			if (getCkbNormalVoucher().isSelected()) {
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.errmessage");
				tempvo.setDatas(null);
				t.addElement(tempvo);
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.discardflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.tempsaveflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
			}
			if (getCkbErrorVoucher().isSelected()) {
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");

				tempvo.setIsAnd(UFBoolean.FALSE);

				tempvo.setRestrictfield("gl_voucher.errmessage");
				tempvo.setDatas(null);
				t.addElement(tempvo);
			}
			if (getCkbAbandonVoucher().isSelected()) {
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");

				tempvo.setIsAnd(UFBoolean.FALSE);

				tempvo.setRestrictfield("gl_voucher.discardflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
			}
			if (getCkbTempVoucher().isSelected()) {
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.FALSE);

				tempvo.setRestrictfield("gl_voucher.tempsaveflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
			}

			tempvo = new QueryElementVO();
			tempvo.setOperator(")");

			tempvo.setIsAnd(UFBoolean.FALSE);
			t.addElement(tempvo);
		}
		if (!getCmBDifflag().getSelectedItem().equals(
				NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
						"02002003-0255"))) {
			if (getCmBDifflag().getSelectedItem().equals(
					NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55",
							"UPP2002gl55-000320"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setOperator("(");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.isdifflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setOperator(")");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);
			} else {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setOperator("(");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.FALSE);
				tempvo.setRestrictfield("gl_voucher.isdifflag");
				tempvo.setDatas(new String[] { "N" });
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.FALSE);
				tempvo.setRestrictfield("gl_voucher.isdifflag");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setOperator(")");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);
			}
		}
		if (!getM_convertComboBox().getSelectedItem().equals(
				NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000278"))) {
			if (getM_convertComboBox().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000730"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setOperator("(");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.convertflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setOperator(")");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);
			} else if (getM_convertComboBox().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000731"))) {
				QueryElementVO tempvo = new QueryElementVO();

				tempvo = new QueryElementVO();
				tempvo.setOperator("(");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.convertflag");
				tempvo.setDatas(new String[] { "N" });
				t.addElement(tempvo);
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.FALSE);
				tempvo.setRestrictfield("gl_voucher.convertflag");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setOperator(")");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);
			}
		}
		if (!getM_convertComboBoxs().getSelectedItem().equals(
				NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000278"))) {
			if (getM_convertComboBoxs().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("2002100557",
							"UPP2002100557-000075"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setOperator("(");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.FALSE);
				tempvo.setRestrictfield("gl_voucher.pk_sourcepk");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setOperator(")");
				tempvo.setIsAnd(UFBoolean.TRUE);
				t.addElement(tempvo);
			} else if (getM_convertComboBoxs().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("2002100557",
							"UPP2002100557-000076"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_sourcepk");
				tempvo.setDatas(null);
				t.addElement(tempvo);
			}

		}

		if (!getCmbVoucherState().getSelectedItem().equals(
				NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000278"))) {
			if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000279"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_manager");
				tempvo.setDatas(null);
				t.addElement(tempvo);
			} else if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000280"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_manager");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				if (VoucherDataCenter
						.isTallyAfterChecked(getSelected_pk_orgbook())) {
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("!=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.pk_checked");
					tempvo.setDatas(null);
					t.addElement(tempvo);

					if (VoucherDataCenter
							.isRequireCasherSigned(getSelected_pk_orgbook())) {
						if (VoucherDataCenter
								.getVoucherFlowControlModel(getSelected_pk_orgbook()) == 1) {
							tempvo = new QueryElementVO();
							tempvo.setOperator("(");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setOperator("(");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setDatatype("String");
							tempvo.setOperator("!=");
							tempvo.setIsAnd(UFBoolean.TRUE);
							tempvo.setRestrictfield("gl_voucher.pk_checked");
							tempvo.setDatas(null);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setDatatype("String");
							tempvo.setOperator("!=");
							tempvo.setIsAnd(UFBoolean.TRUE);
							tempvo.setRestrictfield("gl_voucher.pk_casher");
							tempvo.setDatas(null);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setDatatype("String");
							tempvo.setOperator("=");
							tempvo.setIsAnd(UFBoolean.TRUE);
							tempvo.setRestrictfield("gl_voucher.signflag");
							tempvo.setDatas(new String[] { "Y" });
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setOperator(")");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);

							tempvo = new QueryElementVO();
							tempvo.setOperator("(");
							tempvo.setIsAnd(UFBoolean.valueOf(false));
							t.addElement(tempvo);

							tempvo = new QueryElementVO();
							tempvo.setDatatype("String");
							tempvo.setOperator("!=");
							tempvo.setIsAnd(UFBoolean.valueOf(false));
							tempvo.setRestrictfield("gl_voucher.signflag");
							tempvo.setDatas(new String[] { "Y" });
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setDatatype("String");
							tempvo.setOperator("!=");
							tempvo.setIsAnd(UFBoolean.TRUE);
							tempvo.setRestrictfield("gl_voucher.pk_checked");
							tempvo.setDatas(null);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setOperator(")");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setOperator(")");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);
						} else {
							tempvo = new QueryElementVO();
							tempvo.setOperator("(");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setOperator("(");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setDatatype("String");
							tempvo.setOperator("!=");
							tempvo.setIsAnd(UFBoolean.TRUE);
							tempvo.setRestrictfield("gl_voucher.pk_casher");
							tempvo.setDatas(null);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setDatatype("String");
							tempvo.setOperator("=");
							tempvo.setIsAnd(UFBoolean.TRUE);
							tempvo.setRestrictfield("gl_voucher.signflag");
							tempvo.setDatas(new String[] { "Y" });
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setOperator(")");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setDatatype("String");
							tempvo.setOperator("!=");
							tempvo.setIsAnd(UFBoolean.valueOf(false));
							tempvo.setRestrictfield("gl_voucher.signflag");
							tempvo.setDatas(new String[] { "Y" });
							t.addElement(tempvo);
							tempvo = new QueryElementVO();
							tempvo.setOperator(")");
							tempvo.setIsAnd(UFBoolean.TRUE);
							t.addElement(tempvo);
						}
					}

				} else if (VoucherDataCenter
						.isRequireCasherSigned(getSelected_pk_orgbook())) {
					if (VoucherDataCenter
							.getVoucherFlowControlModel(getSelected_pk_orgbook()) == 1) {
						tempvo = new QueryElementVO();
						tempvo.setOperator("(");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setDatatype("String");
						tempvo.setOperator("!=");
						tempvo.setIsAnd(UFBoolean.TRUE);
						tempvo.setRestrictfield("gl_voucher.pk_checked");
						tempvo.setDatas(null);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setDatatype("String");
						tempvo.setOperator("!=");
						tempvo.setIsAnd(UFBoolean.TRUE);
						tempvo.setRestrictfield("gl_voucher.pk_casher");
						tempvo.setDatas(null);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setOperator(")");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
					} else {
						tempvo = new QueryElementVO();
						tempvo.setOperator("(");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setOperator("(");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setDatatype("String");
						tempvo.setOperator("!=");
						tempvo.setIsAnd(UFBoolean.TRUE);
						tempvo.setRestrictfield("gl_voucher.pk_casher");
						tempvo.setDatas(null);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setDatatype("String");
						tempvo.setOperator("=");
						tempvo.setIsAnd(UFBoolean.TRUE);
						tempvo.setRestrictfield("gl_voucher.signflag");
						tempvo.setDatas(new String[] { "Y" });
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setOperator(")");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setDatatype("String");
						tempvo.setOperator("!=");
						tempvo.setIsAnd(UFBoolean.valueOf(false));
						tempvo.setRestrictfield("gl_voucher.signflag");
						tempvo.setDatas(new String[] { "Y" });
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setOperator(")");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
					}
				}

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.discardflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.errmessage");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.tempsaveflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
			} else if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000281"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_manager");
				tempvo.setDatas(null);
				t.addElement(tempvo);
			} else if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000282"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_checked");
				tempvo.setDatas(null);
				t.addElement(tempvo);
			} else if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000283"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_manager");
				tempvo.setDatas(null);
				t.addElement(tempvo);
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_checked");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				if (VoucherDataCenter
						.getVoucherFlowControlModel(getSelected_pk_orgbook()) == 0) {
					if (VoucherDataCenter
							.isRequireCasherSigned(getSelected_pk_orgbook())) {
						tempvo = new QueryElementVO();
						tempvo.setOperator("(");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setOperator("(");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setDatatype("String");
						tempvo.setOperator("!=");
						tempvo.setIsAnd(UFBoolean.TRUE);
						tempvo.setRestrictfield("gl_voucher.pk_casher");
						tempvo.setDatas(null);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setDatatype("String");
						tempvo.setOperator("=");
						tempvo.setIsAnd(UFBoolean.TRUE);
						tempvo.setRestrictfield("gl_voucher.signflag");
						tempvo.setDatas(new String[] { "Y" });
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setOperator(")");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setDatatype("String");
						tempvo.setOperator("!=");
						tempvo.setIsAnd(UFBoolean.FALSE);
						tempvo.setRestrictfield("gl_voucher.signflag");
						tempvo.setDatas(new String[] { "Y" });
						t.addElement(tempvo);
						tempvo = new QueryElementVO();
						tempvo.setOperator(")");
						tempvo.setIsAnd(UFBoolean.TRUE);
						t.addElement(tempvo);
					}

				} else if (VoucherDataCenter
						.isRequireCasherSigned(getSelected_pk_orgbook())) {
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.pk_casher");
					tempvo.setDatas(null);
					t.addElement(tempvo);
				}

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.discardflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.errmessage");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.tempsaveflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
			} else if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000284"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_checked");
				tempvo.setDatas(null);
				t.addElement(tempvo);
			} else if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000285"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_casher");
				tempvo.setDatas(null);
				t.addElement(tempvo);
			} else if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000286"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_manager");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				if (VoucherDataCenter
						.getVoucherFlowControlModel(getSelected_pk_orgbook()) == 0) {
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.pk_checked");
					tempvo.setDatas(null);
					t.addElement(tempvo);
				} else if (VoucherDataCenter
						.isTallyAfterChecked(getSelected_pk_orgbook())) {
					tempvo = new QueryElementVO();
					tempvo.setDatatype("String");
					tempvo.setOperator("!=");
					tempvo.setIsAnd(UFBoolean.TRUE);
					tempvo.setRestrictfield("gl_voucher.pk_checked");
					tempvo.setDatas(null);
					t.addElement(tempvo);
				}

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_casher");
				tempvo.setDatas(null);
				t.addElement(tempvo);
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.signflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.discardflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.errmessage");
				tempvo.setDatas(null);
				t.addElement(tempvo);

				tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("!=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.tempsaveflag");
				tempvo.setDatas(new String[] { "Y" });
				t.addElement(tempvo);
			} else if (getCmbVoucherState().getSelectedItem().equals(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000287"))) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_voucher.pk_casher");
				tempvo.setDatas(null);
				t.addElement(tempvo);
			}
		}
		QueryElementVO[] result = new QueryElementVO[t.size()];
		t.copyInto(result);
		return result;
	}

	private FreeValueList getFreeValueList1() {
		if (this.ivjFreeValueList1 == null) {
			try {
				this.ivjFreeValueList1 = new FreeValueList();
				this.ivjFreeValueList1.setName("FreeValueList1");

				this.ivjFreeValueList1.setNoshowcode(true);
				this.ivjFreeValueList1
						.setPreferredSize(new Dimension(200, 121));
				this.ivjFreeValueList1.setHideColum();
				this.ivjFreeValueList1.setSealDataFlag(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjFreeValueList1;
	}

	private UILabel getLAttachment() {
		if (this.ivjLAttachment == null) {
			try {
				this.ivjLAttachment = new UILabel();
				this.ivjLAttachment.setName("LAttachment");
				this.ivjLAttachment.setText(NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000289"));

				this.ivjLAttachment.setBounds(10, 155, 60, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLAttachment;
	}

	private UILabel getLCasher() {
		if (this.ivjLCasher == null) {
			try {
				this.ivjLCasher = new UILabel();
				this.ivjLCasher.setName("LCasher");
				this.ivjLCasher.setText(NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000290"));

				this.ivjLCasher.setBounds(360, 95, 40, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLCasher;
	}

	private UILabel getLChecked() {
		if (this.ivjLChecked == null) {
			try {
				this.ivjLChecked = new UILabel();
				this.ivjLChecked.setName("LChecked");
				this.ivjLChecked.setText(NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000291"));

				this.ivjLChecked.setBounds(360, 125, 40, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLChecked;
	}

	private UILabel getLCorp() {
		if (this.ivjLCorp == null) {
			try {
				this.ivjLCorp = new UILabel();
				this.ivjLCorp.setName("LCorp");
				this.ivjLCorp.setText(NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000179"));

				this.ivjLCorp.setBounds(10, 10, 60, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLCorp;
	}

	private UILabel getLPrepared() {
		if (this.ivjLPrepared == null) {
			try {
				this.ivjLPrepared = new UILabel();
				this.ivjLPrepared.setName("LPrepared");
				this.ivjLPrepared.setText(NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000293"));

				this.ivjLPrepared.setBounds(360, 65, 40, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLPrepared;
	}

	private UILabel getLSystem() {
		if (this.ivjLSystem == null) {
			try {
				this.ivjLSystem = new UILabel();
				this.ivjLSystem.setName("LSystem");
				this.ivjLSystem.setText(NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000294"));

				this.ivjLSystem.setBounds(360, 10, 60, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLSystem;
	}

	private UILabel getLTally() {
		if (this.ivjLTally == null) {
			try {
				this.ivjLTally = new UILabel();
				this.ivjLTally.setName("LTally");
				this.ivjLTally.setText(NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000295"));

				this.ivjLTally.setBounds(360, 155, 40, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLTally;
	}

	private UILabel getLTo() {
		if (this.ivjLTo == null) {
			try {
				this.ivjLTo = new UILabel();
				this.ivjLTo.setName("LTo");
				this.ivjLTo.setText("~");
				this.ivjLTo.setBounds(282, 45, 8, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLTo;
	}

	private UILabel getLTo1() {
		if (this.ivjLTo1 == null) {
			try {
				this.ivjLTo1 = new UILabel();
				this.ivjLTo1.setName("LTo1");
				this.ivjLTo1.setText("~");
				this.ivjLTo1.setBounds(170, 155, 8, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLTo1;
	}

	private UILabel getLVoucherno() {
		if (this.ivjLVoucherno == null) {
			try {
				this.ivjLVoucherno = new UILabel();
				this.ivjLVoucherno.setName("LVoucherno");
				this.ivjLVoucherno.setText(NCLangRes.getInstance().getStrByID(
						"20021005", "UPP20021005-000296"));

				this.ivjLVoucherno.setBounds(184, 45, 48, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLVoucherno;
	}

	private UILabel getLVoucherState() {
		if (this.ivjLVoucherState == null) {
			try {
				this.ivjLVoucherState = new UILabel();
				this.ivjLVoucherState.setName("LVoucherState");
				this.ivjLVoucherState.setText(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000297"));

				this.ivjLVoucherState.setBounds(360, 35, 60, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLVoucherState;
	}

	private UILabel getLVouchertype() {
		if (this.ivjLVouchertype == null) {
			try {
				this.ivjLVouchertype = new UILabel();
				this.ivjLVouchertype.setName("LVouchertype");
				this.ivjLVouchertype.setText(NCLangRes.getInstance()
						.getStrByID("20021005", "UPP20021005-000298"));

				this.ivjLVouchertype.setBounds(10, 45, 60, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLVouchertype;
	}

	public String getM_selected_pk_corp() {
		return this.m_selected_pk_corp;
	}

	private String getModulecode() {
		return this.modulecode;
	}

	private UIRefPane getRefAbandon() {
		if (this.ivjRefAbandon == null) {
			try {
				this.ivjRefAbandon = new UIRefPane();
				this.ivjRefAbandon.setName("RefAbandon");
				this.ivjRefAbandon.setLocation(400, 185);
				OperatorDefaultRefModel model = new OperatorDefaultRefModel(
						"操作员");
				this.ivjRefAbandon.setRefModel(model);

				this.ivjRefAbandon.setVisible(false);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRefAbandon;
	}

	private UIRefPane getRefCasher() {
		if (this.ivjRefCasher == null) {
			try {
				this.ivjRefCasher = new UIRefPane();
				this.ivjRefCasher.setName("RefCasher");
				this.ivjRefCasher.setLocation(404, 95);
				this.ivjRefCasher.setSize(new Dimension(114, 20));
				OperatorDefaultRefModel model = new OperatorDefaultRefModel(
						"操作员");
				this.ivjRefCasher.setRefModel(model);
				setRefCasher();
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRefCasher;
	}

	private UIRefPane getRefChecked() {
		if (this.ivjRefChecked == null) {
			try {
				this.ivjRefChecked = new UIRefPane();
				this.ivjRefChecked.setName("RefChecked");
				this.ivjRefChecked.setLocation(404, 125);
				this.ivjRefChecked.setSize(new Dimension(114, 20));

				OperatorDefaultRefModel model = new OperatorDefaultRefModel(
						"操作员");
				this.ivjRefChecked.setRefModel(model);
				setRefChecker();
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRefChecked;
	}

	public UIRefPane getRefCorp() {
		if (this.ivjRefCorp == null) {
			try {
				this.ivjRefCorp = new UIRefPane();
				this.ivjRefCorp.setName("RefCorp");
				this.ivjRefCorp.setBounds(70, 10, 100, 20);

				this.ivjRefCorp.setRefNodeName("总账核算账簿");
				this.ivjRefCorp.setDataPowerOperation_code("FI");
				if (StringUtils.isEmpty(this.m_pk_glorgbook))
					this.ivjRefCorp.setPK(GlWorkBench.getDefaultMainOrg());
				else {
					this.ivjRefCorp.setPK(this.m_pk_glorgbook);
				}

				this.ivjRefCorp.setTreeGridNodeMultiSelected(true);

				this.ivjRefCorp.setMultiSelectedEnabled(true);
				this.ivjRefCorp.setDisabledDataButtonShow(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRefCorp;
	}

	private UIRefPane getRefPrepared() {
		if (this.ivjRefPrepared == null) {
			try {
				this.ivjRefPrepared = new UIRefPane();
				this.ivjRefPrepared.setName("RefPrepared");
				this.ivjRefPrepared.setLocation(404, 65);
				this.ivjRefPrepared.setSize(new Dimension(114, 20));

				OperatorDefaultRefModel model = new OperatorDefaultRefModel(
						"操作员");
				this.ivjRefPrepared.setRefModel(model);
				setRefPrepared();
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRefPrepared;
	}

	private SystemComboBox getRefSystem() {
		if (this.ivjRefSystem == null) {
			try {
				this.ivjRefSystem = new SystemComboBox();
				this.ivjRefSystem.setName("RefSystem");
				this.ivjRefSystem.setLocation(420, 9);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRefSystem;
	}

	private UIRefPane getRefTally() {
		if (this.ivjRefTally == null) {
			try {
				this.ivjRefTally = new UIRefPane();
				this.ivjRefTally.setName("RefTally");
				this.ivjRefTally.setLocation(404, 155);
				this.ivjRefTally.setSize(new Dimension(114, 20));

				OperatorDefaultRefModel model = new OperatorDefaultRefModel(
						"操作员");
				this.ivjRefTally.setRefModel(model);
				setRefTallyed();
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRefTally;
	}

	public String getSelected_pk_corp() {
		if (getRefCorp().getRefPKs() == null)
			return VoucherDataCenter.getClientPk_orgbook();
		String pk_orgbook = getRefCorp().getRefPK();
		String[] pk_orgbooks = getRefCorp().getRefPKs();
		if (Arrays.asList(pk_orgbooks).contains(
				VoucherDataCenter.getClientPk_orgbook())) {
			pk_orgbook = VoucherDataCenter.getClientPk_orgbook();
		} else {
			pk_orgbook = pk_orgbooks[0];
		}

		return pk_orgbook;
	}

	public String getSelected_pk_orgbook() {
		return getRefCorp().getRefPK() == null ? VoucherDataCenter
				.getClientPk_orgbook() : getRefCorp().getRefPK();
	}

	public QueryElementVO[] getSelfConditionVOs() {
		Vector vecR = new Vector();
		QueryElementVO[] qvos = getSelfDefQueryPanel1().getConditionVOs();
		if (qvos != null) {
			if ((getBURefPane().getRefPKs() != null)
					&& (getBURefPane().getRefPKs().length > 0)) {
				QueryElementVO tempvo = new QueryElementVO();
				tempvo.setDatatype("String");
				tempvo.setOperator("=");
				tempvo.setIsAnd(UFBoolean.TRUE);
				tempvo.setRestrictfield("gl_detail.pk_unit");
				tempvo.setDatas(getBURefPane().getRefPKs());
				vecR.addElement(tempvo);
			}
			for (int i = 0; i < qvos.length; i++) {
				vecR.addElement(qvos[i]);
			}

		}

		if ((getFreeValueList1().getTable().isEditing())
				&& (getFreeValueList1().getTable().getCellEditor() != null)) {
			getFreeValueList1().getTable().getCellEditor().stopCellEditing();
		}
		AssVO[] assvo = getFreeValueList1().getAllValueAssVo();
		if ((assvo != null) && (assvo.length > 0)) {
			QueryElementVO tmpvo = new QueryElementVO();
			tmpvo.setIsAnd(UFBoolean.TRUE);
			tmpvo.setOperator("(");
			tmpvo.setRestrictfield("gl_freevalue");
			vecR.addElement(tmpvo);
			String[] data = new String[assvo.length];
			for (int i = 0; i < assvo.length; i++) {
				data[i] = (assvo[i].getPk_Checktype() + (assvo[i]
						.getPk_Checkvalue() == null ? "" : assvo[i]
						.getPk_Checkvalue()));
			}
			tmpvo = new QueryElementVO();
			tmpvo.setDatatype("String");
			tmpvo.setIsAnd(UFBoolean.FALSE);
			tmpvo.setOperator("=");
			tmpvo.setRestrictfield("gl_freevalue.checktype+gl_freevalue.checkvalue");
			tmpvo.setDatas(data);
			vecR.addElement(tmpvo);
			tmpvo = new QueryElementVO();
			tmpvo.setIsAnd(UFBoolean.TRUE);
			tmpvo.setOperator(")");
			tmpvo.setRestrictfield("gl_freevalue");
			vecR.addElement(tmpvo);
		}
		QueryElementVO[] rvo = (QueryElementVO[]) null;
		if (vecR.size() > 0) {
			rvo = new QueryElementVO[vecR.size()];
			vecR.copyInto(rvo);
		}
		return rvo;
	}

	private SelfDefQueryPanel getSelfDefQueryPanel1() {
		if (this.ivjSelfDefQueryPanel1 == null) {
			try {
				this.ivjSelfDefQueryPanel1 = new SelfDefQueryPanel();
				this.ivjSelfDefQueryPanel1.setName("SelfDefQueryPanel1");
				this.ivjSelfDefQueryPanel1.setLocation(0, 0);
				getSelfDefQueryPanel1().setQueryElements(
						new int[] { 109, 103, 123, 104, 310, 313, 323 });
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjSelfDefQueryPanel1;
	}

	private UITextField getTBeginAttachment() {
		if (this.ivjTBeginAttachment == null) {
			try {
				this.ivjTBeginAttachment = new UITextField();
				this.ivjTBeginAttachment.setName("TBeginAttachment");

				this.ivjTBeginAttachment
						.setPreferredSize(new Dimension(100, 22));
				this.ivjTBeginAttachment.setTextType("TextInt");
				this.ivjTBeginAttachment.setMaxLength(20);

				this.ivjTBeginAttachment.setDelStr("-");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjTBeginAttachment;
	}

	private UITextField getTEndAttachment() {
		if (this.ivjTEndAttachment == null) {
			try {
				this.ivjTEndAttachment = new UITextField();
				this.ivjTEndAttachment.setName("TEndAttachment");
				this.ivjTEndAttachment.setText("");

				this.ivjTEndAttachment.setPreferredSize(new Dimension(100, 22));

				this.ivjTEndAttachment.setTextType("TextInt");
				this.ivjTEndAttachment.setDelStr("-");
				this.ivjTEndAttachment.setMaxLength(20);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjTEndAttachment;
	}

	private UITextField getTVouchernofrom() {
		if (this.ivjTVouchernofrom == null) {
			try {
				this.ivjTVouchernofrom = new UITextField();
				this.ivjTVouchernofrom.setName("TVouchernofrom");
				this.ivjTVouchernofrom.setText("");
				this.ivjTVouchernofrom.setPreferredSize(new Dimension(56, 22));

				this.ivjTVouchernofrom.setTextType("TextInt");
				this.ivjTVouchernofrom.setMaxLength(9);
				this.ivjTVouchernofrom.setFormatShow(false);
				this.ivjTVouchernofrom.setDelStr("-");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjTVouchernofrom;
	}

	private UITextField getTVouchernoto() {
		if (this.ivjTVouchernoto == null) {
			try {
				this.ivjTVouchernoto = new UITextField();
				this.ivjTVouchernoto.setName("TVouchernoto");
				this.ivjTVouchernoto.setPreferredSize(new Dimension(56, 22));

				this.ivjTVouchernoto.setTextType("TextInt");
				this.ivjTVouchernoto.setMaxLength(9);
				this.ivjTVouchernoto.setFormatShow(false);
				this.ivjTVouchernoto.setDelStr("-");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjTVouchernoto;
	}

	public LayoutPanel getUIPanel1() {
		if (this.ivjUIPanel1 == null) {
			this.ivjUIPanel1 = new LayoutPanel(14, 6);
			this.ivjUIPanel1.setComPreferredSize(new Dimension(120, 22));

			getUIPanel1().add(1, 1, getLCorp());
			getUIPanel1().add(1, 2, getRefCorp());
			getUIPanel1().add(1, 3, getLabel());
			getUIPanel1().add(1, 4, getBURefPane());
			getUIPanel1().add(1, 5, getLSystem());
			getUIPanel1().add(1, 6, getRefSystem());

			getUIPanel1().add(2, 1, getLVouchertype());
			getUIPanel1().add(2, 2, getCmbVouchertype());

			IndividualSetting indivSettings = null;
			try {
				indivSettings = IndividuationManager.getIndividualSetting(
						new DefaultConfigPage().getClass(), false);
				String pk = indivSettings.getString("df_voucher_type");

				getCmbVouchertype().setPK(pk);
			} catch (BusinessException e) {
				e.printStackTrace();
			}

			getUIPanel1().add(2, 3, getLVoucherno());

			UIPanel tempPanel = new UIPanel(new FlowLayout(0, 0, 0));
			tempPanel.add(getTVouchernofrom());
			tempPanel.add(getLTo());
			tempPanel.add(getTVouchernoto());

			getUIPanel1().add(2, 4, tempPanel);
			getUIPanel1().add(2, 5, getLVoucherState());
			getUIPanel1().add(2, 6, getCmbVoucherState());

			getUIPanel1().add(3, 1, getYearPeriodDatePane1(), 2, 4);
			getUIPanel1().add(3, 5, getLPrepared());
			getUIPanel1().add(3, 6, getRefPrepared());
			getUIPanel1().add(4, 5, getLCasher());
			getUIPanel1().add(4, 6, getRefCasher());

			getUIPanel1().add(5, 1, getLAttachment());

			UIPanel tempPanel1 = new UIPanel(new FlowLayout(0, 0, 0));
			tempPanel1.add(getTBeginAttachment());
			tempPanel1.add(getLTo1());
			tempPanel1.add(getTEndAttachment());

			getUIPanel1().add(5, 2, tempPanel1, 1, 3);
			getUIPanel1().add(5, 5, getLChecked());
			getUIPanel1().add(5, 6, getRefChecked());

			UIPanel tempPanel2 = new UIPanel(new FlowLayout(0, 0, 0));
			tempPanel2.add(getCkbNormalVoucher());
			tempPanel2.add(getCkbErrorVoucher());
			tempPanel2.add(getCkbAbandonVoucher());
			tempPanel2.add(getCkbTempVoucher());

			getUIPanel1().add(6, 1, tempPanel2, 1, 4);
			getUIPanel1().add(6, 5, getLTally());
			getUIPanel1().add(6, 6, getRefTally());

			this.UILabel = new UILabel();
			this.UILabel.setText(NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"2002gl55", "UPP2002gl55-000320"));

			this.UILabel2 = new UILabel();
			this.UILabel2.setText(NCLangRes.getInstance().getStrByID(
					"2002100557", "UPP2002100557-000077"));

			this.UILabel1 = new UILabel();
			this.UILabel1.setText(NCLangRes.getInstance().getStrByID(
					"2002100557", "UPP2002100557-000078"));

			getUIPanel1().add(7, 1, this.UILabel1);
			getUIPanel1().add(7, 2, getM_convertComboBox());
			getUIPanel1().add(7, 3, this.UILabel2);
			getUIPanel1().add(7, 4, getM_convertComboBoxs());
			getUIPanel1().add(7, 5, this.UILabel);
			getUIPanel1().add(7, 6, getCmBDifflag());

			getUIPanel1().addVoucherQueryCells(8, 1,
					getSelfDefQueryPanel1().getComponents(), 3);
			getUIPanel1().add(8, 5, getFreeValueList1(), 7, 2);
		}

		return this.ivjUIPanel1;
	}

	private UIPanel getUIPanel2() {
		if (this.ivjUIPanel2 == null) {
			try {
				this.ivjUIPanel2 = new UIPanel();
				this.ivjUIPanel2.setName("UIPanel2");
				this.ivjUIPanel2.setPreferredSize(new Dimension(10, 190));
				this.ivjUIPanel2.setLayout(new BorderLayout());
				getUIPanel2().add(getUIPanel4(), "East");
				getUIPanel2().add(getUIPanel6(), "West");
				getUIPanel2().add(getUIScrollPane1(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjUIPanel2;
	}

	private UIPanel getUIPanel4() {
		if (this.ivjUIPanel4 == null) {
			try {
				this.ivjUIPanel4 = new UIPanel();
				this.ivjUIPanel4.setName("UIPanel4");
				this.ivjUIPanel4.setPreferredSize(new Dimension(240, 421));
				this.ivjUIPanel4.setLayout(new BorderLayout());

				getUIPanel4().add(getUIPanel5(), "East");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjUIPanel4;
	}

	private UIPanel getUIPanel5() {
		if (this.ivjUIPanel5 == null) {
			try {
				this.ivjUIPanel5 = new UIPanel();
				this.ivjUIPanel5.setName("UIPanel5");
				this.ivjUIPanel5.setPreferredSize(new Dimension(15, 10));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjUIPanel5;
	}

	private UIPanel getUIPanel6() {
		if (this.ivjUIPanel6 == null) {
			try {
				this.ivjUIPanel6 = new UIPanel();
				this.ivjUIPanel6.setName("UIPanel6");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjUIPanel6;
	}

	private UIScrollPane getUIScrollPane1() {
		if (this.ivjUIScrollPane1 == null) {
			try {
				this.ivjUIScrollPane1 = new UIScrollPane();
				this.ivjUIScrollPane1.setName("UIScrollPane1");
				getUIScrollPane1().setViewportView(getSelfDefQueryPanel1());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjUIScrollPane1;
	}

	public YearPeriodDatePane getYearPeriodDatePane1() {
		if (this.ivjYearPeriodDatePane1 == null) {
			try {
				this.ivjYearPeriodDatePane1 = new YearPeriodDatePane();
				this.ivjYearPeriodDatePane1.setName("YearPeriodDatePane1");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjYearPeriodDatePane1;
	}

	private void handleException(Throwable exception) {
		if ((exception instanceof GlBusinessException)) {
			FiUif2MsgUtil.showUif2DetailMessage(this, "",
					exception.getMessage());
		}
		Logger.debug("--------- 未捕捉到的异常 ---------");
		Logger.error(exception);
	}

	private void initConnections() throws Exception {
		getRefCorp().addValueChangedListener(this.ivjEventHandler);
		getBURefPane().addValueChangedListener(this.ivjEventHandler);
		getYearPeriodDatePane1().getUIRefPane1().addValueChangedListener(
				this.ivjEventHandler);
		getYearPeriodDatePane1().getUIRefPane2().addValueChangedListener(
				this.ivjEventHandler);
	}

	private void initialize() {
		try {
			setName("VoucherQueryConditionPanel");
			setLayout(new BorderLayout());
			if (("simpchn".equals(GlWorkBench.getCurrLanguage().getCode()))
					|| ("tradchn".equals(GlWorkBench.getCurrLanguage()
							.getCode())))
				setSize(600, 565);
			else {
				setSize(820, 565);
			}
			add(getUIPanel1(), "Center");
			initConnections();

			getYearPeriodDatePane1().setShowMode(0);
			getYearPeriodDatePane1().refresh(getRefCorp().getRefPK());
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		getRefSystem().addItem(
				NCLangRes.getInstance().getStrByID("20021005",
						"UPP20021005-000288"), new DapsystemVO());

		getRefSystem().addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (VoucherQueryConditionPanel.this.getRefSystem().getVos() == null) {
					VoucherQueryConditionPanel.this.getRefSystem().refresh();
					VoucherQueryConditionPanel.this.getRefSystem().updateUI();
				}
			}

			public void focusLost(FocusEvent e) {
			}
		});
		getCkbAbandonVoucher().setSelected(true);
		getCkbErrorVoucher().setSelected(true);
		getCkbNormalVoucher().setSelected(true);
		getCkbTempVoucher().setSelected(true);
	}

	public void refCorp_ValueChanged(ValueChangedEvent event) {
		String[] pk_orgbooks = getRefCorp().getRefPKs();
		if ((pk_orgbooks == null) || (pk_orgbooks.length == 0)) {
			MessageDialog.showErrorDlg(
					this,
					NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55",
							"UPP2002gl55-000344"),
					NCLangRes4VoTransl.getNCLangRes().getStrByID("2002gl55",
							"UPP2002gl55-000376"));

			return;
		}

		String pk_orgbook = getRefCorp().getRefPK();
		try {
			GlRemoteCallProxy.callProxy(CacheRequestFactory
					.newBookChangeContextVO(getModulecode(), pk_orgbook));
			GlRemoteCallProxy.asynchCallProxy(CacheRequestFactory
					.newAddVoucherContextVO(getModulecode(), pk_orgbook));
		} catch (BusinessException e1) {
			Logger.error(e1.getMessage(), e1);
		}

		if (Arrays.asList(pk_orgbooks).contains(
				VoucherDataCenter.getClientPk_orgbook())) {
			pk_orgbook = VoucherDataCenter.getClientPk_orgbook();
		} else {
			pk_orgbook = pk_orgbooks[0];
		}

		getYearPeriodDatePane1().refresh(pk_orgbook);
		try {
			GlPeriodVO pvo = new GlPeriodForClient().getPeriod(pk_orgbook,
					GlWorkBench.getBusiDate());
			getYearPeriodDatePane1().setBeginyear(pvo.getYear());
			getYearPeriodDatePane1().setBeginperiod(pvo.getMonth());
			getYearPeriodDatePane1().setEndyear(pvo.getYear());
			getYearPeriodDatePane1().setEndperiod(pvo.getMonth());
		} catch (Exception localException1) {
		}
		getCmbVouchertype().setPk_org(pk_orgbook);

		String t_pk_corp = VoucherDataCenter
				.getPk_corpByPk_glorgbook(pk_orgbook);
		this.m_selected_pk_corp = t_pk_corp;
		getRefAbandon().getRefModel().setRefNodeName("人员", t_pk_corp);
		getRefAbandon().getUITextField().setText("");
		getRefCasher().getRefModel().setRefNodeName("人员", t_pk_corp);
		getRefCasher().getUITextField().setText("");
		getRefChecked().getRefModel().setRefNodeName("人员", t_pk_corp);
		getRefChecked().getUITextField().setText("");
		getRefPrepared().getRefModel().setRefNodeName("人员", t_pk_corp);
		getRefPrepared().getUITextField().setText("");
		getRefTally().getRefModel().setRefNodeName("人员", t_pk_corp);
		getRefTally().getUITextField().setText("");

		setRefCasher();
		setRefChecker();
		setRefPrepared();
		setRefTallyed();
		getFreeValueList1().stopEditing();
		try {
			getFreeValueList1().setAllCheckTypesByorgbook(pk_orgbook,
					GlWorkBench.getBusiDate().toStdString());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage(), e);
		}
		getSelfDefQueryPanel1().setPk_orgbook(pk_orgbook);
		resetBusiunit(pk_orgbook);
	}

	public void refBU_ValueChanged(ValueChangedEvent event) {
		String pk_orgbook = getRefCorp().getRefPK();
		try {
			getFreeValueList1().setAllCheckTypesByorgbook(pk_orgbook,
					getBURefPane().getRefPK(),
					GlWorkBench.getBusiDate().toStdString());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage(), e);
		}
	}

	private void resetBusiunit(String pk_orgbook) {
		if ((getRefCorp().getRefPKs() != null)
				&& (getRefCorp().getRefPKs().length > 1)) {
			getLabel().setVisible(false);
			getBURefPane().setVisible(false);
			getBURefPane().setPK(null);
			return;
		}

		if ((!StringUtils.isEmpty(pk_orgbook))
				&& (GLParaDataCache.getInstance().isSecondBUSupport(pk_orgbook)
						.booleanValue())) {
			getLabel().setVisible(true);
			getBURefPane().setVisible(true);
		} else {
			getLabel().setVisible(false);
			getBURefPane().setVisible(false);
		}
		((GLBUVersionWithBookRefModel) getBURefPane().getRefModel())
				.setPk_accountingbook(pk_orgbook);
		((GLBUVersionWithBookRefModel) getBURefPane().getRefModel())
				.setVstartdate(getYearPeriodDatePane1().getEnddate().asEnd());
	}

	public void setModulecode(String newModulecode) {
		if (newModulecode.indexOf("20023040") >= 0) {
			getCkbAbandonVoucher().setSelected(false);
			getCkbAbandonVoucher().setVisible(false);

			getCkbTempVoucher().setVisible(false);
			getCkbTempVoucher().setSelected(false);
		} else if (newModulecode.indexOf("20020VSIGN") >= 0) {
			getCmbVoucherState().setSelectedItem(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000286"));
		} else if (newModulecode.indexOf("20020CHECK") >= 0) {
			getCmbVoucherState().setSelectedItem(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000283"));
		} else if (newModulecode.indexOf("20020TALLY") >= 0) {
			getCmbVoucherState().setSelectedItem(
					NCLangRes.getInstance().getStrByID("20021005",
							"UPP20021005-000280"));
		} else if (newModulecode.indexOf("20021QUERY") >= 0) {
			((GlAccountingBookRefModel) getRefCorp().getRefModel())
					.setSealedDataShow(true);
		}
		this.modulecode = newModulecode;
		if (this.modulecode == "20023040") {
			getRefCorp().setMultiSelectedEnabled(false);
			getRefCorp().setTreeGridNodeMultiSelected(false);
		}

		GlAccountingBookRefModel model = (GlAccountingBookRefModel) getRefCorp()
				.getRefModel();
		model.setFuncCode(this.modulecode);
		model.setResourceID("AccountingBook");
		getRefCorp().setRefModel(model);
	}

	private void setRefCasher() {
		if (!StringUtils.isEmpty(getSelected_pk_corp()))
			this.ivjRefCasher
					.getRefModel()
					.setWherePart(
							"  sm_user.cuserid in (select userid from gl_voucheruser where pk_accountingbook='"
									+ getSelected_pk_corp()
									+ "' and type="
									+ 2
									+ " )");
		else
			this.ivjRefCasher.getRefModel().setWherePart(" 1 = 2 ");
	}

	private void setRefChecker() {
		if (!StringUtils.isEmpty(getSelected_pk_corp()))
			this.ivjRefChecked
					.getRefModel()
					.setWherePart(
							"  sm_user.cuserid in (select userid from gl_voucheruser where pk_accountingbook='"
									+ getSelected_pk_corp()
									+ "' and type="
									+ 1
									+ " )");
		else
			this.ivjRefChecked.getRefModel().setWherePart(" 1 = 2 ");
	}

	private void setRefPrepared() {
		String pk_glorgbook = null;
		if (!StringUtils.isEmpty(getSelected_pk_corp())) {
			if (this.ivjRefPrepared.getRefModel() != null)
				this.ivjRefPrepared
						.getRefModel()
						.setWherePart(
								"  sm_user.cuserid in (select userid from gl_voucheruser where pk_accountingbook='"
										+ getSelected_pk_corp()
										+ "' and type="
										+ 0 + " )");
		} else
			this.ivjRefPrepared.getRefModel().setWherePart(" 1 = 2 ");
	}

	private void setRefTallyed() {
		if (!StringUtils.isEmpty(getSelected_pk_corp()))
			this.ivjRefTally
					.getRefModel()
					.setWherePart(
							"  sm_user.cuserid in (select userid from gl_voucheruser where pk_accountingbook='"
									+ getSelected_pk_corp()
									+ "' and type="
									+ 3
									+ " )");
		else
			this.ivjRefTally.getRefModel().setWherePart(" 1 = 2 ");
	}

	public void setPk_units(String[] pk_units) {
		getBURefPane().setPKs(pk_units);
	}

	public void setPk_orgbook(String pk_orgbook, String stddate) {
		if (StringUtils.isEmpty(pk_orgbook))
			return;
		getRefCorp().setPK(pk_orgbook);
		getYearPeriodDatePane1().refresh(pk_orgbook);
		getCmbVouchertype().setPk_org(pk_orgbook);
		this.m_pk_glorgbook = pk_orgbook;
		setRefCasher();
		setRefChecker();
		setRefPrepared();
		setRefTallyed();
		getFreeValueList1().stopEditing();
		try {
			getFreeValueList1().setAllCheckTypesByorgbook(
					getSelected_pk_orgbook(), stddate);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(e.getMessage());
		}
		getSelfDefQueryPanel1().setPk_orgbook(pk_orgbook);
		resetBusiunit(pk_orgbook);
	}

	public void setPk_user(String pk_user) {
	}

	public void setRefCorpEditable(boolean editable) {
		getRefCorp().setEditable(editable);
	}

	public void setRefCorpEnabled(boolean editable) {
		getRefCorp().setEnabled(editable);
	}

	public void setYearPeriod(String year, String period) {
		getYearPeriodDatePane1().setBeginyear(year);
		getYearPeriodDatePane1().setBeginperiod(period);
	}

	public void setBeginYear(String year) {
		getYearPeriodDatePane1().setBeginyear(year);
	}

	public void setBeginPeriod(String period) {
		getYearPeriodDatePane1().setBeginperiod(period);
	}

	public void setBeginDate(UFDate date) {
		getYearPeriodDatePane1().setBegindate(date);
		getYearPeriodDatePane1().setSelectionState(1);
	}

	public void setEndYear(String year) {
		getYearPeriodDatePane1().setEndyear(year);
	}

	public void setEndPeriod(String period) {
		getYearPeriodDatePane1().setEndperiod(period);
	}

	public void setEndDate(UFDate date) {
		getYearPeriodDatePane1().setEnddate(date);
	}

	public void setPKSystem(String pk_system) {
		try {
			getRefSystem().setSelectedPk(pk_system);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public void setVoucherState(String state) {
		getCmbVoucherState().setSelectedItem(state);
	}

	public void setFreevalueListVisible(boolean b) {
		getUIPanel4().setVisible(b);
		updateUI();
	}

	public void setFreevalueVO(AssVO[] vos) {
		try {
			getFreeValueList1().setAssVOs(vos);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public void setConditionVO(int key, QueryElementVO queryElementVo) {
		getSelfDefQueryPanel1().setConditionVO(key, queryElementVo);
	}

	public void setSelfQueryElements(int[] qe) {
		getSelfDefQueryPanel1().setQueryElements(qe);
	}

	private UIComboBox getM_convertComboBox() {
		if (this.m_convertComboBox == null) {
			this.m_convertComboBox = new UIComboBox();
			this.m_convertComboBox.setSize(new Dimension(80, 20));
			this.m_convertComboBox.setLocation(new Point(70, 211));
			this.m_convertComboBox.addItem(NCLangRes.getInstance().getStrByID(
					"20021005", "UPP20021005-000278"));

			this.m_convertComboBox.addItem(NCLangRes.getInstance().getStrByID(
					"20021005", "UPP20021005-000730"));

			this.m_convertComboBox.addItem(NCLangRes.getInstance().getStrByID(
					"20021005", "UPP20021005-000731"));
		}

		return this.m_convertComboBox;
	}

	private UIComboBox getM_convertComboBoxs() {
		if (this.m_convertComboBoxs == null) {
			this.m_convertComboBoxs = new UIComboBox();
			this.m_convertComboBoxs.setLocation(new Point(215, 211));
			this.m_convertComboBoxs.setSize(new Dimension(80, 20));
			this.m_convertComboBoxs.addItem(NCLangRes.getInstance().getStrByID(
					"20021005", "UPP20021005-000278"));

			this.m_convertComboBoxs.addItem(NCLangRes.getInstance().getStrByID(
					"2002100557", "UPP2002100557-000075"));

			this.m_convertComboBoxs.addItem(NCLangRes.getInstance().getStrByID(
					"2002100557", "UPP2002100557-000076"));
		}

		return this.m_convertComboBoxs;
	}

	private UIComboBox getCmBDifflag() {
		if (this.CmBDifflag == null) {
			this.CmBDifflag = new UIComboBox();
			this.CmBDifflag.setBounds(new Rectangle(368, 211, 100, 20));
			this.CmBDifflag.addItem(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2002gl55", "UPP2002gl55-000011"));

			this.CmBDifflag.addItem(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2002gl55", "UPP2002gl55-000320"));

			this.CmBDifflag.addItem(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("2002gl55", "UPP2002gl55-000377"));
		}

		return this.CmBDifflag;
	}

	private UILabel getLabel() {
		if (this.label == null) {
			this.label = new UILabel();
			this.label.setText(NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"common", "UCMD1-000396"));
			this.label.setBounds(176, 10, 48, 22);
		}
		return this.label;
	}

	private UIRefPane getBURefPane() {
		if (this.buRefPane == null) {
			this.buRefPane = new UIRefPane("总账业务单元版本");

			this.buRefPane.setBounds(232, 10, 106, 20);
			this.buRefPane.setMultiSelectedEnabled(true);
			this.buRefPane.getRefModel().setPkFieldCode("pk_org");
			this.buRefPane.setDataPowerOperation_code("fi");
			resetBusiunit(GlWorkBench.getDefaultMainOrg());
		}

		return this.buRefPane;
	}

	public HashMap<String, Object> getConditionMap() {
		HashMap conditionMap = new HashMap();
		conditionMap.put(getRefCorp().getName(), getRefCorp().getRefPK());
		conditionMap.put(getCmbVouchertype().getName(), getCmbVouchertype()
				.getRefPK());
		conditionMap.put(getTVouchernoto().getName(), getTVouchernoto()
				.getText());
		conditionMap.put(getCkbNormalVoucher().getName(),
				Boolean.valueOf(getCkbNormalVoucher().isSelected()));
		conditionMap.put(getCkbErrorVoucher().getName(),
				Boolean.valueOf(getCkbErrorVoucher().isSelected()));
		conditionMap.put(getCkbAbandonVoucher().getName(),
				Boolean.valueOf(getCkbAbandonVoucher().isSelected()));
		conditionMap.put(getCkbTempVoucher().getName(),
				Boolean.valueOf(getCkbTempVoucher().isSelected()));
		conditionMap.put(getCmbVoucherState().getName(),
				Integer.valueOf(getCmbVoucherState().getSelectedIndex()));
		conditionMap.put(getRefPrepared().getName(), getRefPrepared()
				.getRefPK());
		conditionMap.put(getRefAbandon().getName(), getRefAbandon().getRefPK());
		conditionMap.put(getRefCasher().getName(), getRefCasher().getRefPK());
		conditionMap.put(getRefChecked().getName(), getRefChecked().getRefPK());
		conditionMap.put(getRefTally().getName(), getRefTally().getRefPK());
		conditionMap.put(getRefSystem().getName(),
				Integer.valueOf(getRefSystem().getSelectedIndex()));
		conditionMap.put(getTBeginAttachment().getName(), getTBeginAttachment()
				.getText());
		conditionMap.put(getTEndAttachment().getName(), getTEndAttachment()
				.getText());
		conditionMap.put("getM_convertComboBoxs",
				Integer.valueOf(getM_convertComboBoxs().getSelectedIndex()));
		conditionMap.put("getCmBDifflag",
				Integer.valueOf(getCmBDifflag().getSelectedIndex()));
		conditionMap.put("getBURefPane", getBURefPane().getRefPK());

		QueryElementVO[] selfConditionVOs = getSelfConditionVOs();

		if ((selfConditionVOs != null) && (selfConditionVOs.length > 0)) {
			for (QueryElementVO queryElementVO : selfConditionVOs) {
				String restrictfield = queryElementVO.getRestrictfield();
				if (restrictfield != null) {
					if (restrictfield.equals("gl_detail.explanation")) {
						String[] datas = queryElementVO.getDatas();
						List rtList = new ArrayList();
						if ((datas != null) && (datas.length > 0)) {
							for (String string : datas) {
								rtList.add(string.replaceAll("%", ""));
							}
							queryElementVO.setDatas((String[]) rtList
									.toArray(new String[0]));
						}
						conditionMap.put("gl_detail.explanation",
								queryElementVO);
					} else if (restrictfield.equals("bd_account.code")) {
						conditionMap.put("bd_account.code", queryElementVO);
					}
				}
			}

		}

		return conditionMap;
	}

	public void setConditionMap(HashMap<String, Object> conditionMapt) {
		HashMap conditionMap = conditionMapt;
		if (conditionMap.containsKey(getRefCorp().getName())) {
			getRefCorp().setPK(conditionMap.get(getRefCorp().getName()));
		} else {
			getRefCorp().setPK(null);
		}
		if (conditionMap.containsKey(getCmbVouchertype().getName())) {
			getCmbVouchertype().setPK(
					conditionMap.get(getCmbVouchertype().getName()));
		} else {
			getCmbVouchertype().setPK(null);
		}
		if (conditionMap.containsKey(getTVouchernoto().getName())) {
			getTVouchernoto().setText(
					(String) conditionMap.get(getTVouchernoto().getName()));
		} else {
			getTVouchernoto().setText("");
		}

		if (conditionMap.containsKey(getRefCorp().getName())) {
			getCkbNormalVoucher().setSelected(
					((Boolean) conditionMap
							.get(getCkbNormalVoucher().getName()))
							.booleanValue());
		} else {
			getCkbNormalVoucher().setSelected(false);
		}

		if (conditionMap.containsKey(getCkbErrorVoucher().getName())) {
			getCkbErrorVoucher()
					.setSelected(
							((Boolean) conditionMap.get(getCkbErrorVoucher()
									.getName())).booleanValue());
		} else {
			getCkbErrorVoucher().setSelected(false);
		}

		if (conditionMap.containsKey(getCkbAbandonVoucher().getName())) {
			getCkbAbandonVoucher().setSelected(
					((Boolean) conditionMap.get(getCkbAbandonVoucher()
							.getName())).booleanValue());
		} else {
			getCkbAbandonVoucher().setSelected(false);
		}

		if (conditionMap.containsKey(getCkbTempVoucher().getName())) {
			getCkbTempVoucher().setSelected(
					((Boolean) conditionMap.get(getCkbTempVoucher().getName()))
							.booleanValue());
		} else {
			getCkbTempVoucher().setSelected(false);
		}

		if (conditionMap.containsKey(getCmbVoucherState().getName())) {
			getCmbVoucherState()
					.setSelectedIndex(
							((Integer) conditionMap.get(getCmbVoucherState()
									.getName())).intValue());
		} else {
			getCmbVoucherState().setSelectedIndex(0);
		}

		if (conditionMap.containsKey(getRefPrepared().getName())) {
			getRefPrepared()
					.setPK(conditionMap.get(getRefPrepared().getName()));
		} else {
			getRefPrepared().setPK(null);
		}

		if (conditionMap.containsKey(getRefAbandon().getName())) {
			getRefAbandon().setPK(conditionMap.get(getRefAbandon().getName()));
		} else {
			getRefAbandon().setPK(null);
		}

		if (conditionMap.containsKey(getRefCasher().getName())) {
			getRefCasher().setPK(conditionMap.get(getRefCasher().getName()));
		} else {
			getRefCasher().setPK(null);
		}

		if (conditionMap.containsKey(getRefChecked().getName())) {
			getRefChecked().setPK(conditionMap.get(getRefChecked().getName()));
		} else {
			getRefChecked().setPK(null);
		}

		if (conditionMap.containsKey(getRefTally().getName())) {
			getRefTally().setPK(conditionMap.get(getRefTally().getName()));
		} else {
			getRefTally().setPK(null);
		}
		if (conditionMap.containsKey(getRefSystem().getName())) {
			ModuleVO[] vos = getRefSystem().getVos();
			if (vos == null) {
				getRefSystem().refresh();
				getRefSystem().updateUI();
			}
			getRefSystem().setSelectedIndex(
					((Integer) conditionMap.get(getRefSystem().getName()))
							.intValue());
		} else {
			getRefSystem().setSelectedIndex(0);
		}

		if (conditionMap.containsKey(getTBeginAttachment().getName())) {
			getTBeginAttachment().setText(
					(String) conditionMap.get(getTBeginAttachment().getName()));
		} else {
			getTBeginAttachment().setText("");
		}

		if (conditionMap.containsKey(getTEndAttachment().getName())) {
			getTEndAttachment().setText(
					(String) conditionMap.get(getTEndAttachment().getName()));
		} else {
			getTEndAttachment().setText("");
		}
		if (conditionMap.containsKey("getM_convertComboBoxs")) {
			getM_convertComboBoxs().setSelectedIndex(
					((Integer) conditionMap.get("getM_convertComboBoxs"))
							.intValue());
		} else {
			getM_convertComboBoxs().setSelectedIndex(0);
		}

		if (conditionMap.containsKey("getM_convertComboBoxs")) {
			getM_convertComboBoxs().setSelectedIndex(
					((Integer) conditionMap.get("getM_convertComboBoxs"))
							.intValue());
		} else {
			getM_convertComboBoxs().setSelectedIndex(0);
		}
		if (conditionMap.containsKey("getBURefPane")) {
			getBURefPane().setPK((String) conditionMap.get("getBURefPane"));
		} else {
			getBURefPane().setPK("");
		}

		if (conditionMap.containsKey("gl_detail.explanation"))
			getSelfDefQueryPanel1().setConditionVO(109,
					(QueryElementVO) conditionMap.get("gl_detail.explanation"));
		else {
			getSelfDefQueryPanel1().setConditionVO(109, new QueryElementVO());
		}

		if (conditionMap.containsKey("bd_account.code"))
			getSelfDefQueryPanel1().setConditionVO(103,
					(QueryElementVO) conditionMap.get("bd_account.code"));
		else
			getSelfDefQueryPanel1().setConditionVO(103, new QueryElementVO());
	}

	public String getPk_accountingbook() {
		return getRefCorp().getRefPK();
	}

	class IvjEventHandler implements ValueChangedListener {
		IvjEventHandler() {
		}

		public void valueChanged(ValueChangedEvent event) {
			if (event.getSource() == VoucherQueryConditionPanel.this
					.getRefCorp())
				VoucherQueryConditionPanel.this.connEtoC1(event);
			else if (event.getSource() == VoucherQueryConditionPanel.this
					.getBURefPane())
				VoucherQueryConditionPanel.this.connEtoC2(event);
			else if (event.getSource() == VoucherQueryConditionPanel.this
					.getYearPeriodDatePane1().getUIRefPane1())
				VoucherQueryConditionPanel.this.connEtoC3(event);
			else if (event.getSource() == VoucherQueryConditionPanel.this
					.getYearPeriodDatePane1().getUIRefPane2())
				VoucherQueryConditionPanel.this.connEtoC4(event);
		}
	}
}