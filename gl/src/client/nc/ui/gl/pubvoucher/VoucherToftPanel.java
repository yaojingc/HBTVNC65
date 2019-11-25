package nc.ui.gl.pubvoucher;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nc.bs.logging.Logger;
import nc.individuation.defaultData.DefaultConfigPage;
import nc.individuation.property.pub.IndividualSetting;
import nc.individuation.property.pub.IndividuationManager;
import nc.ui.gl.common.NCHoteKeyRegistCenter;
import nc.ui.gl.datacache.OrgVersionCache;
import nc.ui.gl.gateway.glworkbench.GlWorkBench;
import nc.ui.gl.uicfg.ValueChangeEvent;
import nc.ui.gl.uicfg.ValueChangedAdaptor;
import nc.ui.gl.uicfg.voucher.VoucherCell;
import nc.ui.gl.uicfg.voucher.VoucherTablePane;
import nc.ui.gl.vouchercard.IVoucherModel;
import nc.ui.gl.vouchercard.IVoucherView;
import nc.ui.gl.vouchercard.VoucherButtonLoader;
import nc.ui.gl.vouchercard.VoucherModel;
import nc.ui.gl.vouchercard.VoucherPanel;
import nc.ui.gl.vouchercard.VoucherTabbedPane;
import nc.ui.gl.vouchercard.VoucherUI;
import nc.ui.gl.voucherdata.VoucherDataBridge;
import nc.ui.gl.vouchermodels.IMasterModel;
import nc.ui.gl.vouchertools.VoucherDataCenter;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.SeparatorButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.sm.clientsetup.ClientSetup;
import nc.ui.sm.clientsetup.ClientSetupCache;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.bd.vouchertype.VoucherTypeVO;
import nc.vo.fipub.utils.uif2.FiUif2MsgUtil;
import nc.vo.gateway60.accountbook.AccountBookUtil;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.gl.pubvoucher.VoucherModflagTool;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.gl.uicfg.ButtonRegistVO;
import nc.vo.gl.uicfg.SeparatorButtonRegistVO;
import nc.vo.gl.uicfg.UIConfigVO;
import nc.vo.gl.uicfg.voucher.VoucherConfigVO;
import nc.vo.gl.voucher.VoucherDataPreLoadVO;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
// 制单新增
public class VoucherToftPanel extends ToftPanel implements ValueChangeListener,
		IUiPanel {
	private static final long serialVersionUID = 4075910443879820191L;
	private VoucherPanel m_voucherpanel = null;

	private VoucherTabbedPane tabVoucher = null;
	private IParent m_parent;
	protected boolean isUILoaded = false;
	private String strFunctionName;
	private HashMap button_function_cache = new HashMap();

	private HashMap function_button_cache = new HashMap();

	UIConfigVO uiconfigvo = null;

	public VoucherToftPanel() {
		initialize();
	}

	public void addListener(Object objListener, Object objUserdata) {
		getVoucherPanel().addListener(objUserdata, objListener);
	}

	public void close() {
		if (this.m_parent != null)
			this.m_parent.closeMe();
		else
			getFrame().getApplet().destroy();
	}

	public void doFunction(String functioncode) {
		getVoucherPanel().doOperation(functioncode);
	}

	public String getFunctionName() {
		if (this.strFunctionName == null) {
			try {
				this.strFunctionName = getParameter("functionname");
			} catch (Exception e) {
				this.strFunctionName = "voucherbridge";
			}
			if (this.strFunctionName == null)
				this.strFunctionName = "voucherbridge";
		}
		return this.strFunctionName;
	}

	public String getTitle() {
		return null;
	}

	public VoucherPanel getVoucherPanel() {
		return getTabVoucher().getFirstVoucherPanel();
	}

	public VoucherPanel getCurrentVoucherPanel() {
		return getTabVoucher().getCurrentVoucherPanel();
	}

	private void initButtons() {
		this.button_function_cache.clear();
		this.function_button_cache.clear();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		add(getTabVoucher(), "Center");
		setVoucherPanel(new VoucherPanel());
		getVoucherPanel().addListener("ToftPanel", this);

		getVoucherPanel().getVoucherModel().addValueChangeListener(this);
	}

	public void installButtonFunction(ButtonObject button, String functioncode) {
		if (functioncode == null)
			return;
		getCurrentVoucherPanel().installOperation(functioncode);
		this.button_function_cache.put(button, functioncode);
		this.function_button_cache.put(functioncode, button);
	}

	public void init() {
		setButtons(getButtons());
	}

	public void installButtons(ButtonObject parentButton,
			ButtonRegistVO[] buttons) {
		if (buttons == null)
			return;
		ButtonObject[] bbs = new ButtonObject[buttons.length];
		for (int i = 0; i < buttons.length; i++) {
			if ((buttons[i] instanceof SeparatorButtonRegistVO))
				bbs[i] = new SeparatorButtonObject();
			else {
				bbs[i] = new ButtonObject(buttons[i].getButtonName(),
						buttons[i].getButtonName(), 2);
			}
			installButtonFunction(bbs[i], buttons[i].getButtonFunction());
			bbs[i].setHint(buttons[i].getButtonTag());
			bbs[i].setCode(buttons[i].getButtonFuncCode() == null ? buttons[i]
					.getButtonName() : buttons[i].getButtonFuncCode());
			if (buttons[i].getChildButton() != null) {
				installButtons(bbs[i], buttons[i].getChildButton());
			}
		}
		if (parentButton == null) {
			NCHoteKeyRegistCenter.buildAction(this, bbs);
			setButtons(bbs);
		} else {
			parentButton.addChileButtons(bbs);
		}
	}

	public void installFunction(String functioncode) {
		getVoucherPanel().installOperation(functioncode);
	}

	public void openVoucher(VoucherVO voucher, boolean canEdit) {
		if (voucher == null) {
			return;
		}

		getVoucherPanel().addValueChangeListener(this.m_parent);
		getVoucherPanel().getVoucherUI().setParent(this.m_parent);

		if (!canEdit)
			getVoucherPanel().setEditable(false);
		getVoucherPanel().setM_modulecode(getModuleCode());
		getVoucherPanel().setVO(voucher);
		getTabVoucher().setVoucherPanel(getVoucherPanel());
		if (!canEdit)
			getVoucherPanel().setEditable(false);
		updateUI();
	}

	public Object invoke(Object objData, Object objUserData) {
		String userid = GlWorkBench.getLoginUser();
		Boolean ispower = new Boolean(false);
		if (((String) objUserData).equals(NCLangRes.getInstance().getStrByID(
				"20021005", "UPP20021005-000037"))) {
			showHintMessage(NCLangRes.getInstance().getStrByID("2002100555",
					"UPP2002100555-000055"));
			getTabVoucher().setVoucherPanel(getVoucherPanel());
			getVoucherPanel().stopEditing();
			VoucherVO vo = null;
			try {
				String strVoucherPK = (String) objData;
				vo = VoucherDataBridge.getInstance().queryByPk(strVoucherPK);

				ispower = VoucherDataBridge.getInstance().isAccsubjPower(vo,
						userid);
				if (ispower.booleanValue()) {
					FiUif2MsgUtil.showUif2DetailMessage(
							this,
							NCLangRes.getInstance().getStrByID("2002100555",
									"UPP2002100555-000047"),
							NCLangRes.getInstance().getStrByID("2002100555",
									"UPP2002100555-000056"));
				}
				if (vo.getPk_vouchertype() != null)
					setTitleText(VoucherDataCenter.getVouchertypeByPk_orgbook(
							vo.getPk_accountingbook(), vo.getPk_vouchertype())
							.getName());
			} catch (ClassCastException e) {
				vo = (VoucherVO) objData;

				ispower = VoucherDataBridge.getInstance().isAccsubjPower(vo,
						userid);
				if (ispower.booleanValue()) {
					FiUif2MsgUtil.showUif2DetailMessage(
							this,
							NCLangRes.getInstance().getStrByID("2002100555",
									"UPP2002100555-000047"),
							NCLangRes.getInstance().getStrByID("2002100555",
									"UPP2002100555-000056"));
				}

				if (vo.getPk_vouchertype() != null)
					setTitleText(VoucherDataCenter.getVouchertypeByPk_orgbook(
							vo.getPk_accountingbook(), vo.getPk_vouchertype())
							.getName());
			} catch (GlBusinessException e) {
				throw e;
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new GlBusinessException(e.getMessage(), e);
			}
			try {
				getVoucherPanel().startEditing();
			} catch (GlBusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new GlBusinessException(e.getMessage(), e);
			}

			if ((vo != null) && (ispower.booleanValue())) {
				vo = VoucherDataBridge.getInstance()
						.filterDetailByAccsubjPower(vo, userid);
				getVoucherPanel().setVO(vo);
				getVoucherPanel().setEditable(false);
				updateUI();
				return null;
			}
			getVoucherPanel().setVO(vo);
			updateUI();
		} else if (((String) objUserData).equals(NCLangRes.getInstance()
				.getStrByID("20021005", "UPP20021005-000038"))) {
			showHintMessage(NCLangRes.getInstance().getStrByID("2002100555",
					"UPP2002100555-000057"));
			getTabVoucher().setVoucherPanel(getVoucherPanel());
			getVoucherPanel().stopEditing();
			try {
				String strVoucherPK = (String) objData;
				VoucherVO vo = VoucherDataBridge.getInstance().queryByPk(
						strVoucherPK);
				getVoucherPanel().setVO(vo);
				ispower = VoucherDataBridge.getInstance().isAccsubjPower(vo,
						userid);
				if (ispower.booleanValue()) {
					vo = VoucherDataBridge.getInstance()
							.filterDetailByAccsubjPower(vo, userid);
					getVoucherPanel().setVO(vo);
				}
				if (vo.getPk_vouchertype() != null)
					setTitleText(VoucherDataCenter.getVouchertypeByPk_orgbook(
							vo.getPk_accountingbook(), vo.getPk_vouchertype())
							.getName());
			} catch (ClassCastException e) {
				VoucherVO vo = (VoucherVO) objData;
				getVoucherPanel().setVO(vo);
				ispower = VoucherDataBridge.getInstance().isAccsubjPower(vo,
						userid);
				if (ispower.booleanValue()) {
					vo = VoucherDataBridge.getInstance()
							.filterDetailByAccsubjPower(vo, userid);
					getVoucherPanel().setVO(vo);
				}
				if (vo.getPk_vouchertype() != null)
					setTitleText(VoucherDataCenter.getVouchertypeByPk_orgbook(
							vo.getPk_accountingbook(), vo.getPk_vouchertype())
							.getName());
			} catch (GlBusinessException e) {
				throw e;
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new GlBusinessException(e.getMessage(), e);
			}
			getVoucherPanel().setEditable(false);
			updateUI();
		} else if (((String) objUserData).equals(NCLangRes.getInstance()
				.getStrByID("20021005", "UPP20021005-000039"))) {
			showHintMessage(NCLangRes.getInstance().getStrByID("2002100555",
					"UPP2002100555-000058"));

			getTabVoucher().setVoucherPanel(getVoucherPanel());
			getVoucherPanel().stopEditing();
			VoucherVO vo = new VoucherVO();

			vo.setPk_accountingbook(getVoucherPanel().getPk_accountingbook());
			vo.setPk_org(AccountBookUtil.getPk_orgByAccountBookPk(vo
					.getPk_accountingbook()));

			vo.setPk_group(GlWorkBench.getDefaultGroup());

			vo.setPk_system("2002");
			vo.setPrepareddate(GlWorkBench.getBusiDate());
			vo.setCreationtime(new UFDateTime(GlWorkBench.getDefaultDate()
					.getMillis()));
			Logger.error("业务日期" + GlWorkBench.getBusiDate().toString());
			Logger.error("当前服务器日期" + GlWorkBench.getBusiDate().toString());
			vo.setPeriod(GlWorkBench.getLoginPeriod());

			vo.setNo(null);

			vo.setModifyflag(VoucherModflagTool
					.getVoucherHeadModFlag("YYYYYYYYYYYYYYYYYYYYYYYYYYYY"));
			vo.setAttachment(new Integer(0));
			vo.setTotalcredit(new UFDouble(0));
			vo.setTotaldebit(new UFDouble(0));
			vo.setTotalcreditglobal(UFDouble.ZERO_DBL);
			vo.setTotaldebitglobal(UFDouble.ZERO_DBL);
			vo.setTotalcreditgroup(UFDouble.ZERO_DBL);
			vo.setTotaldebitgroup(UFDouble.ZERO_DBL);
			vo.setYear(GlWorkBench.getLoginYear());
			vo.setPeriod(GlWorkBench.getLoginPeriod());

			if (vo.getPk_accountingbook() != null) {
				String pk_org = AccountBookUtil.getPk_orgByAccountBookPk(vo
						.getPk_accountingbook());
				vo.setPk_org(pk_org);
				HashMap versionMap = null;
				try {
					versionMap = OrgVersionCache.getInstance()
							.getNewVIDSByOrgIDSAndDate(new String[] { pk_org },
									vo.getPrepareddate().asEnd());
				} catch (BusinessException e) {
					Logger.error(e.getMessage(), e);
					throw new GlBusinessException(e.getMessage(), e);
				}
				if (versionMap != null) {
					vo.setPk_org_v((String) versionMap.get(pk_org));
				}

			}

			vo.setPk_prepared(GlWorkBench.getLoginUser());

			vo.setCreator(vo.getPk_prepared());

			vo.setVoucherkind(new Integer(0));
			vo.setDiscardflag(UFBoolean.FALSE);
			vo.setDetailmodflag(UFBoolean.TRUE);
			vo.setVoucherkind(new Integer(0));
			Vector vecdetails = new Vector();
			vo.setDetail(vecdetails);

			VoucherCell cell = (VoucherCell) ((VoucherModel) getVoucherPanel()
					.getVoucherModel()).getView().getVoucherCellEditor(-1,
					new Integer(11));

			IndividualSetting indivSettings = null;
			try {
				indivSettings = IndividuationManager.getIndividualSetting(
						new DefaultConfigPage().getClass(), false);
				String pk = indivSettings.getString("df_voucher_type");

				vo.setPk_vouchertype(pk);
			} catch (BusinessException e) {
				e.printStackTrace();
			}

			getVoucherPanel().getVoucherUI().setEditable(true);
			getVoucherPanel().setVO(vo);
			getVoucherPanel().startEditing();

			getVoucherPanel().getVoucherModel().setSaved(true);
			updateUI();
		} else if (!((String) objUserData).equals("addMessageListener")) {
			if (objUserData.toString().equals("setvouchers")) {
				VoucherVO voucher = ((VoucherVO[]) objData)[0];
				getVoucherPanel().setVO(voucher);
				updateUI();
			} else {
				try {
					if (objUserData.toString().equals("setVoucherPk")) {
						getVoucherPanel().setEditable(false);
					}

				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					throw new GlBusinessException(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	public void nextClosed() {
	}

	public void onButtonClicked(ButtonObject bo) {
		ShowStatusBarMsgUtil.showStatusBarMsg(" ", this);
		try {
			String func = (String) this.button_function_cache.get(bo);
			if (func != null)
				getCurrentVoucherPanel().doOperation(func);
		} catch (GlBusinessException e) {
			Logger.error(e.getMessage(), e);
			FiUif2MsgUtil.showUif2DetailMessage(this, null, e.getMessage());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			FiUif2MsgUtil.showUif2DetailMessage(this, null, e.getMessage());
		}
	}

	public boolean onClosing() {
		VoucherUI voucherCardUi;
		try {
			IVoucherView voucherUi = getVoucherPanel().getVoucherUI();
			if ((voucherUi != null) && ((voucherUi instanceof VoucherUI))) {
				voucherCardUi = (VoucherUI) voucherUi;
				VoucherTablePane voucherTablePane = voucherCardUi
						.getVoucherTablePane();
				TableColumnModel columnModel = voucherTablePane
						.getUITablePane().getTable().getColumnModel();
				int columnCount = columnModel.getColumnCount();

				ClientSetup currentClientSetup = ClientSetupCache
						.getCurrentClientSetup();
				for (int i = 0; i < columnCount; i++) {
					TableColumn column = columnModel.getColumn(i);
					Object identifier = column.getIdentifier();
					int width = column.getWidth();
					currentClientSetup.put(VoucherTablePane.class.getName()
							+ identifier, Integer.valueOf(width));
				}

				int rowHeight = voucherTablePane.getUITablePane().getTable()
						.getRowHeight();
				currentClientSetup.put(VoucherTablePane.class.getName()
						+ "getRowHeight", Integer.valueOf(rowHeight));
			}
		} catch (Exception e) {
			Logger.error(e);
		}
		try {
			if ((getFunctionName().equals("preparevoucher"))
					|| (getFunctionName().equals("voucherbridge"))
					|| (getFunctionName().equals("offsetvoucher"))
					|| (getFunctionName().equals("checkvoucher"))) {
				Object result = getVoucherPanel().doOperation("CONFIRMSAVE");
				if ((result != null) && (result.toString().equals("cancel"))) {
					return false;
				}

			}

		} catch (GlBusinessException e) {
			Logger.error(e.getMessage(), e);
			FiUif2MsgUtil.showUif2DetailMessage(this, null, e.getMessage());
			return false;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			FiUif2MsgUtil.showUif2DetailMessage(this, null, e.getMessage());
			return false;
		} finally {
			VoucherDataCenter.getInstance().clearAll();
			ValueChangedAdaptor.clearListener(this.m_parent);
		}
		VoucherDataCenter.getInstance().clearAll();
		ValueChangedAdaptor.clearListener(this.m_parent);

		return true;
	}

	protected void postInit() {
		if (this.isUILoaded) {
			try {
				UIConfigVO v = GLUIConfigVOTool.loadConfigVO(this.isUILoaded,
						getVoucherPanel().getPk_accountingbook(),
						getModuleCode());
				ButtonRegistVO[] buttons = v.getButtons();
				if (buttons != null)
					installButtons(null, buttons);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}

			return;
		}

		try {
			UIConfigVO v = GLUIConfigVOTool.loadConfigVO(this.isUILoaded,
					getVoucherPanel().getPk_accountingbook(), getModuleCode());

			VoucherDataPreLoadVO addCard = new VoucherDataPreLoadVO();

			addCard.setPk_glorgbook(getVoucherPanel().getPk_accountingbook());
			addCard.setUserid(GlWorkBench.getLoginUser());
			addCard.setModulecode(getModuleCode());
			addCard.setDate(GlWorkBench.getBusiDate());

			if (v == null) {
				Logger.debug("取数据出错");
				throw new GlBusinessException(NCLangRes.getInstance()
						.getStrByID("2002100555", "UPP2002100555-000059"));
			}
			Logger.debug("UIConfigVO=" + v);

			getVoucherPanel().setUIConfigVO(v);
			ButtonRegistVO[] buttons = v.getButtons();
			if (buttons != null)
				installButtons(null, buttons);
			this.uiconfigvo = v;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(NCLangRes.getInstance().getStrByID(
					"20021005", "UPP20021005-000040"));
		}

		getVoucherPanel().installOperation("ADDEMPTYDETAIL");
		getVoucherPanel().installOperation("CONFIRMSAVE");
		getVoucherPanel().installOperation("CLEAROTHERVOUCHERTAB");
		getVoucherPanel().installOperation("REMOVETAB_BY_VOUCHERPK");

		prepareButtons();
		this.isUILoaded = true;
	}

	public void prepareButtons() {
		if (getButtons() == null) {
			ButtonObject[] t_buttons = VoucherButtonLoader
					.getButtons(getFunctionName());
			NCHoteKeyRegistCenter.buildAction(this, t_buttons);
			setButtons(t_buttons);
		}
	}

	public void removeListener(Object objListener, Object objUserdata) {
	}

	public void resetButtonState(VoucherVO vo) {
	}

	public void setVoucherPanel(VoucherPanel newM_voucherpanel) {
		getTabVoucher().setVoucherPanel(newM_voucherpanel);
		this.m_voucherpanel = newM_voucherpanel;
	}

	public void showMe(IParent parent) {
		parent.getUiManager().add(this, getName());
		this.m_parent = parent;
		setFrame(parent.getFrame());
		init(((ToftPanel) parent.getUiManager()).getFuncletContext());

		getVoucherPanel().getMasterModel().setParameter("FuncletContext",
				getFuncletContext());

		getVoucherPanel().addValueChangeListener(parent);
		getVoucherPanel().getVoucherUI().setParent(parent);

		postInit();
	}

	public void valueChanged(ValueChangeEvent evt) {
		switch (((Integer) evt.getKey()).intValue()) {
		case 201:
			if (evt.getNewValue() != null) {
				setTitleText(evt.getNewValue().toString());
			}
			return;
		case 0:
			if (evt.getNewValue() == null) {
				this.m_parent.closeMe();
				return;
			}
			VoucherVO vo = (VoucherVO) evt.getNewValue();
			if ((vo.getPk_vouchertype() != null)
					&& (vo.getPk_accountingbook() != null))
				setTitleText(VoucherDataCenter.getVouchertypeByPk_orgbook(
						vo.getPk_accountingbook(), vo.getPk_vouchertype())
						.getName());
			resetButtonState(vo);
			showHintMessage("");
			return;
		case 55:
			VoucherModel model = (VoucherModel) getVoucherPanel()
					.getVoucherModel();
			if (evt.getNewValue() == null) {
				return;
			}

			getVoucherPanel()
					.setPk_accountingbook(evt.getNewValue().toString());
			this.isUILoaded = false;
			postInit();
			getVoucherPanel().setUIConfigVO(this.uiconfigvo);

			if (((UFBoolean) model.getParameter("needNewVoucher"))
					.booleanValue()) {
				invoke(null,
						NCLangRes.getInstance().getStrByID("20021005",
								"UPP20021005-000039"));
			}

			return;
		}
	}

	public VoucherTabbedPane getTabVoucher() {
		if (this.tabVoucher == null) {
			this.tabVoucher = new VoucherTabbedPane();
			this.tabVoucher.setToftPanel(this);
		}
		return this.tabVoucher;
	}

	private VoucherPanel createVoucherPanel() {
		if (this.uiconfigvo == null) {
			this.uiconfigvo = VoucherConfigVO.getSysDefaultVO(getModuleCode(),
					null, null);
		}
		VoucherPanel vp = this.m_voucherpanel;
		vp.setUIConfigVO(this.uiconfigvo);

		return vp;
	}

	public void disableEdit() {
		ButtonObject[] buttons = getButtons();
		ArrayList captions = new ArrayList();
		captions.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC001-0000108"));
		captions.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC001-0000001"));
		captions.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC001-0000039"));
		captions.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0",
				"02002003-0064"));
		captions.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC001-0000013"));
		captions.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC001-0000012"));
		captions.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC001-0000115"));
		for (int i = 0; i < buttons.length; i++) {
			if (captions.contains(buttons[i].getName()))
				buttons[i].setEnabled(false);
		}
		setButtons(buttons);
	}

	public void setLinkButtons() {
		ButtonObject[] buttons = getButtons();
		List buns = new LinkedList();
		String[] linkButtons = { "凭证", "现金流量分析", "原始凭单", "分录", "辅助信息", "联查",
				"联查单据", "联查余额", "联查序时账", "联查预算", "浏览", "首页", "上一页", "下一页",
				"末页", "打印", "返回" };
		HashMap map = new HashMap();
		for (String str : linkButtons) {
			map.put(str, str);
		}

		ButtonObject[] tmpBtns = (ButtonObject[]) null;
		for (ButtonObject btn : buttons) {
			if (map.get(btn.getCode()) != null) {
				buns.add(btn);
				if (btn.getChildCount() > 0) {
					tmpBtns = btn.getChildButtonGroup();
					for (ButtonObject child : tmpBtns) {
						if (map.get(child.getCode()) == null) {
							btn.removeChildButton(child);
						}
					}
				}
			}
		}
		super.setButtons((ButtonObject[]) buns.toArray(new ButtonObject[0]));
	}

	public void setLinkAddButtons() {
		ButtonObject[] buttons = getButtons();
		List buns = new LinkedList();
		String[] toFilterButtons = { "系统暂存" };
		HashMap map = new HashMap();
		for (String str : toFilterButtons) {
			map.put(str, str);
		}

		ButtonObject[] tmpBtns = (ButtonObject[]) null;
		for (ButtonObject btn : buttons) {
			if (map.get(btn.getCode()) == null) {
				buns.add(btn);
				if (btn.getChildCount() > 0) {
					tmpBtns = btn.getChildButtonGroup();
					for (ButtonObject child : tmpBtns) {
						if (map.get(child.getCode()) != null) {
							btn.removeChildButton(child);
						}
					}
				}
			}
		}
		super.setButtons((ButtonObject[]) buns.toArray(new ButtonObject[0]));
	}
}