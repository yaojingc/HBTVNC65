package nc.bs.er.exp.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nc.bs.er.exp.cjk.ctrl.CjkMainViewCtrl;
import nc.bs.er.exp.util.ExpDatasets2AggVOSerializer;
import nc.bs.er.exp.util.ExpReimruleUtil;
import nc.bs.er.exp.util.ExpUtil;
import nc.bs.er.exp.util.YerBxUIControlUtil;
import nc.bs.er.exp.util.YerMultiVersionUtil;
import nc.bs.er.util.YerUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.itf.bd.psnbankacc.IPsnBankaccPubService;
import nc.itf.bd.psnbankacc.IPsnBankaccQueryService;
import nc.itf.fi.pub.Currency;
import nc.pubitf.uapbd.ICustomerPubService;
import nc.pubitf.uapbd.ISupplierPubService;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.formular.LfwFormulaParser;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.serializer.impl.Datasets2AggVOSerializer;
import nc.ui.erm.billpub.action.HRATCommonUtils;
import nc.ui.erm.billpub.action.ServiceFeeTaxUtils;
import nc.vo.arap.bx.util.BodyEditVO;
import nc.vo.arap.bx.util.ControlBodyEditVO;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.psnbankacc.PsnBankaccUnionVO;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.BxcontrastVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.er.exception.ExceptionHandler;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import uap.lfw.core.ml.LfwResBundle;
import uap.web.bd.pub.AppUtil;

public class ExpBodyValueChangeCmd extends UifCommand {
	private String masterDsID;
	private DatasetEvent datasetEvent;
	private BodyAfterEditEventForPortal bodyAfterEditEvent;

	public ExpBodyValueChangeCmd(String masterDsID,
			DatasetEvent datasetCellEvent) {
		this.masterDsID = masterDsID;
		this.datasetEvent = datasetCellEvent;
		this.bodyAfterEditEvent = new BodyAfterEditEventForPortal();
	}

	public void execute() {
		
		String hasBusitemGrid = (String) AppUtil.getAppAttr("ExpHasBusitemGrid");
		if ("N".equals(hasBusitemGrid)) {
			return;
		}
		DatasetCellEvent datasetCellEvent = (DatasetCellEvent) this.datasetEvent;
		if ((datasetCellEvent.getNewValue() == null)
				&& (datasetCellEvent.getOldValue() == null)) {
			return;
		}
		if ((datasetCellEvent.getNewValue() != null)
				&& (datasetCellEvent.getNewValue().equals(datasetCellEvent
						.getOldValue()))) {
			return;
		}
		Dataset busitemDs = (Dataset) datasetCellEvent.getSource();
		
		// 得到当前正在编辑的字段对象
		Field currentField = busitemDs.getFieldSet().getField(
				datasetCellEvent.getColIndex());

		if ((busitemDs.getCurrentRowData() == null)
				|| (busitemDs.getCurrentRowData().getRows() == null)
				|| (busitemDs.getCurrentRowData().getRows().length == 0)) {
			return;
		}
		LfwWindow jkbxWindow = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		LfwView widget = jkbxWindow.getView("main");
		Dataset masterDs = widget.getViewModels().getDataset(this.masterDsID);
		String pkItem = (String) masterDs.getSelectedRow().getValue(
				masterDs.nameToIndex("pk_item"));
		if ((("busitem".equals(busitemDs.getId())) || ("jk_busitem"
				.equals(busitemDs.getId())))
				&& (pkItem != null)
				&& (!"".equals(pkItem))) {
			Map formularMap = (HashMap) LfwRuntimeEnvironment.getWebContext()
					.getRequest().getSession().getAttribute("yer_formularMap");
			Set<String> keySet = formularMap.keySet();
			for (String key : keySet) {
				int keyIndex = busitemDs.nameToIndex(key);
				if (datasetCellEvent.getColIndex() == keyIndex) {
					YerUtil.modifyField(busitemDs, "EditFormular", key,
							(String) formularMap.get(key));
				}
			}

			if (currentField.getEditFormular() != null) {
				processEditorFormular(busitemDs);
			}
		}
		
		dealBusitemChange(datasetCellEvent, masterDs, busitemDs, widget);
		
		
		// 广电劳务费相关开发均需要通过交易类型过滤别的节点不生效
		String djlxbm = masterDs.getSelectedRow().getString(masterDs.nameToIndex("djlxbm"));
		if(HRATCommonUtils.isNotEmpty(djlxbm)){
			if(ServiceFeeTaxUtils.TRANSTYPE.equals(djlxbm)){
				/**
				 * 新增一个portal端表体编辑后事件,单独处理portal端业务
				 * currentField 当前正在编辑的字段
				 */
				bodyAfterEditEvent.doEdit(currentField.getField());
			}
		}
		
		
		
		
	}
	

	private void dealBusitemChange(DatasetCellEvent datasetCellEvent,
			Dataset masterDs, Dataset busitemDs, LfwView widget) {
		int index = datasetCellEvent.getRowIndex();
		if (index >= busitemDs.getRowCount()) {
			return;
		}

		Field field = busitemDs.getFieldSet().getField(
				datasetCellEvent.getColIndex());
		if ((field != null) && (field.getDataType() != null)
				&& ("UFDouble".equals(field.getDataType()))) {
			UFDouble newValue = new UFDouble(
					datasetCellEvent.getNewValue() == null ? ""
							: datasetCellEvent.getNewValue().toString());
			UFDouble oldValue = new UFDouble(
					datasetCellEvent.getOldValue() == null ? ""
							: datasetCellEvent.getOldValue().toString());
			if (newValue.equals(oldValue)) {
				return;
			}
		}

		Row row = busitemDs.getCurrentRowData().getRow(index);

		int amountIndex = busitemDs.nameToIndex("amount");
		if (datasetCellEvent.getColIndex() == amountIndex) {
			dealReimRuleTips(index);

			setHeadTotalValue();

			UFDouble amount = (UFDouble) row.getValue(amountIndex);
			row.setValue(busitemDs.nameToIndex("ybje"), amount);
			modifyFinValues(busitemDs.nameToIndex("ybje"), index, busitemDs,
					row);
			try {
				doContract(busitemDs, row);
			} catch (BusinessException e1) {
				Logger.error(e1.getMessage(), e1);
				throw new LfwRuntimeException(e1);
			}
		}

		if (datasetCellEvent.getColIndex() == busitemDs.nameToIndex("ybje")) {
			setHeadYbjeValue();
			modifyFinValues(busitemDs.nameToIndex("ybje"), index, busitemDs,
					row);
			try {
				doContract(busitemDs, row);
			} catch (BusinessException e1) {
				Logger.error(e1.getMessage(), e1);
				throw new LfwRuntimeException(e1);
			}

		}

		String bxdj = (String) AppUtil.getAppAttr("exp_reimrule_flag");
		if ((bxdj != null) && ("Y".equals(bxdj))) {
			Map ReimRuleMap = (Map) LfwRuntimeEnvironment.getWebContext()
					.getRequest().getSession()
					.getAttribute("yer_ReimRuleBusitemMap");
			if ((ReimRuleMap != null) && (ReimRuleMap.size() > 0)) {
				Set<String> keySet = ReimRuleMap.keySet();
				for (String key : keySet) {
					int itemIndex = busitemDs.nameToIndex(key);
					if (datasetCellEvent.getColIndex() == itemIndex) {
						ExpReimruleUtil.processReimRule(
								masterDs,
								ExpUtil.getAllDetailDss(widget,
										masterDs.getId()),
								masterDs.getSelectedRow());

						ExpReimruleUtil.processReimRule(busitemDs, row,
								this.masterDsID);
						dealReimRuleTips(index);
					}

				}

			}

		}

		int jobid = busitemDs.nameToIndex("jobid");
		if (datasetCellEvent.getColIndex() == jobid) {
			row.setValue(busitemDs.nameToIndex("projecttask"), null);
		}

		if (datasetCellEvent.getColIndex() == busitemDs
				.nameToIndex("pk_pcorg_v")) {
			String pk_pcorg_v = (String) datasetCellEvent.getNewValue();
			String pk_pcorg = YerMultiVersionUtil.getBillHeadFinanceOrg(
					"pk_pcorg_v", pk_pcorg_v, widget, this.masterDsID);
			ExpUtil.setRowValue(row, busitemDs, "pk_pcorg", pk_pcorg);

			ExpUtil.setRowValue(row, busitemDs, "pk_checkele", null);
		}

		if (datasetCellEvent.getColIndex() == busitemDs.nameToIndex("pk_pcorg")) {
			String pk_pcorg = (String) datasetCellEvent.getNewValue();
			UFDate date = (UFDate) masterDs.getSelectedRow().getValue(
					masterDs.nameToIndex("djrq"));
			if ((date == null) || (StringUtil.isEmpty(date.toString()))) {
				date = ExpUtil.getBusiDate();
			}
			String pk_org = (String) masterDs.getSelectedRow().getValue(
					masterDs.nameToIndex("pk_org"));
			String pk_pcorg_v_value = YerMultiVersionUtil
					.getBillHeadFinanceOrgVersion(JKBXHeaderVO.PK_PCORG_V,
							pk_pcorg, date, masterDs, pk_org, widget);
			ExpUtil.setRowValue(row, busitemDs, "pk_pcorg_v", pk_pcorg_v_value);
			String hesdPcorg = (String) masterDs.getSelectedRow().getValue(
					masterDs.nameToIndex("pk_pcorg"));

			if (pk_pcorg.equals(hesdPcorg)) {
				String hesdpk_resacostcenter = (String) masterDs
						.getSelectedRow()
						.getValue(
								masterDs.nameToIndex(BXHeaderVO.PK_RESACOSTCENTER));
				ExpUtil.setRowValue(row, busitemDs, "pk_resacostcenter",
						hesdpk_resacostcenter);
			} else {
				ExpUtil.setRowValue(row, busitemDs, "pk_resacostcenter", null);
			}
		}
		if ("bxzb".equals(masterDs.getId())) {
			if (datasetCellEvent.getColIndex() == busitemDs.nameToIndex("dwbm")) {
				ExpUtil.setRowValue(row, busitemDs, "deptid", null);
				ExpUtil.setRowValue(row, busitemDs, "jkbxr", null);
				ExpUtil.setRowValue(row, busitemDs, "skyhzh", null);
				ExpUtil.setRowValue(row, busitemDs, "receiver", null);
			}

			if (datasetCellEvent.getColIndex() == busitemDs
					.nameToIndex("deptid")) {
				String deptid = (String) datasetCellEvent.getNewValue();
				String jkbxr = row.getString(busitemDs.nameToIndex("jkbxr"));
				if ((jkbxr != null) && (!"".equals(jkbxr))) {
					String[] values = ExpUtil.getPsnDocInfoById(jkbxr);
					if ((values != null) && (values.length > 0)
							&& (!values[1].equals(deptid))) {
						ExpUtil.setRowValue(row, busitemDs, "jkbxr", null);
					}

				}

			}

			if (datasetCellEvent.getColIndex() == busitemDs
					.nameToIndex("paytarget")) {
				int paytarget = ((Integer) row.getValue(busitemDs
						.nameToIndex("paytarget"))).intValue();
				if (paytarget == 0) {
					ExpUtil.setRowValue(row, busitemDs, "hbbm", null);
					ExpUtil.setRowValue(row, busitemDs, "customer", null);
					ExpUtil.setRowValue(row, busitemDs, "custaccount", null);
					ExpUtil.setRowValue(row, busitemDs, "freecust", null);
					ExpUtil.setRowValue(row, busitemDs, "freecustacc", null);
				} else if (paytarget == 1) {
					ExpUtil.setRowValue(row, busitemDs, "receiver", null);
					ExpUtil.setRowValue(row, busitemDs, "skyhzh", null);
					ExpUtil.setRowValue(row, busitemDs, "customer", null);
					ExpUtil.setRowValue(row, busitemDs, "custaccount", null);
				} else if (paytarget == 2) {
					ExpUtil.setRowValue(row, busitemDs, "hbbm", null);
					ExpUtil.setRowValue(row, busitemDs, "receiver", null);
					ExpUtil.setRowValue(row, busitemDs, "skyhzh", null);
					ExpUtil.setRowValue(row, busitemDs, "custaccount", null);
				}
			}

			if (datasetCellEvent.getColIndex() == busitemDs
					.nameToIndex("receiver")) {
				int paytarget = ((Integer) row.getValue(busitemDs
						.nameToIndex("paytarget"))).intValue();
				if (paytarget == 0) {
					ExpUtil.setRowValue(row, busitemDs, "hbbm", null);
					ExpUtil.setRowValue(row, busitemDs, "customer", null);
					ExpUtil.setRowValue(row, busitemDs, "custaccount", null);
					String tSkyhzhAcc = getSkyhzhAcc(row, busitemDs);
					row.setValue(busitemDs.nameToIndex("skyhzh"), tSkyhzhAcc);
				} else if (paytarget == 3) {
					String tSkyhzhAcc = getSkyhzhAcc(row, busitemDs);
					row.setValue(busitemDs.nameToIndex("skyhzh"), tSkyhzhAcc);
				} else {
					ExpUtil.setRowValue(row, busitemDs, "receiver", null);
					ExpUtil.setRowValue(row, busitemDs, "skyhzh", null);
				}
			}

			if (datasetCellEvent.getColIndex() == busitemDs.nameToIndex("hbbm")) {
				String hbbm = (String) datasetCellEvent.getNewValue();
				if ((hbbm == null) || ("".equals(hbbm.trim()))) {
					String customer = (String) row.getValue(busitemDs
							.nameToIndex("customer"));
					if ((customer == null) || ("".equals(customer.trim()))) {
						ExpUtil.setRowValue(row, busitemDs, "custaccount", null);
						ExpUtil.setRowValue(row, busitemDs, "freecust", null);
						ExpUtil.setRowValue(row, busitemDs, "freecustacc", null);
					}
				} else {
					int paytarget = ((Integer) row.getValue(busitemDs
							.nameToIndex("paytarget"))).intValue();
					if (paytarget == 1) {
						ExpUtil.setRowValue(row, busitemDs, "receiver", null);
						ExpUtil.setRowValue(row, busitemDs, "skyhzh", null);
						ExpUtil.setRowValue(row, busitemDs, "customer", null);
						String tAcc = getCustAcc(row, busitemDs, "hbbm");
						row.setValue(busitemDs.nameToIndex("custaccount"), tAcc);
					} else if (paytarget == 3) {
						String tAcc = getCustAcc(row, busitemDs, "hbbm");
						row.setValue(busitemDs.nameToIndex("custaccount"), tAcc);
					} else {
						ExpUtil.setRowValue(row, busitemDs, "hbbm", null);
					}
				}

			}

			if (datasetCellEvent.getColIndex() == busitemDs
					.nameToIndex("customer")) {
				String customer = (String) datasetCellEvent.getNewValue();
				if ((customer == null) || ("".equals(customer.trim()))) {
					String hbbm = (String) row.getValue(busitemDs
							.nameToIndex("hbbm"));
					if ((hbbm == null) || ("".equals(hbbm.trim()))) {
						ExpUtil.setRowValue(row, busitemDs, "custaccount", null);
						ExpUtil.setRowValue(row, busitemDs, "freecust", null);
						ExpUtil.setRowValue(row, busitemDs, "freecustacc", null);
					}
				} else {
					int paytarget = ((Integer) row.getValue(busitemDs
							.nameToIndex("paytarget"))).intValue();
					if (paytarget == 2) {
						ExpUtil.setRowValue(row, busitemDs, "receiver", null);
						ExpUtil.setRowValue(row, busitemDs, "skyhzh", null);
						ExpUtil.setRowValue(row, busitemDs, "hbbm", null);
						String tAcc = getCustAcc(row, busitemDs, "customer");
						row.setValue(busitemDs.nameToIndex("custaccount"), tAcc);
					} else if (paytarget == 3) {
						String tAcc = getCustAcc(row, busitemDs, "customer");
						row.setValue(busitemDs.nameToIndex("custaccount"), tAcc);
					} else {
						ExpUtil.setRowValue(row, busitemDs, "customer", null);
					}
				}
			}
		}
	}

	private String getSkyhzhAcc(Row row, Dataset busitemDs) {
		String receiver = (String) row.getValue(busitemDs
				.nameToIndex("receiver"));
		try {
			List pkBankaccsubList = new ArrayList();
			String dwbm = (String) row.getValue(busitemDs.nameToIndex("dwbm"));
			IPsnBankaccQueryService pbq = (IPsnBankaccQueryService) NCLocator
					.getInstance().lookup(
							IPsnBankaccQueryService.class.getName());
			PsnBankaccUnionVO[] pbus = pbq.queryPsnBankaccUnionVOsByBu(dwbm,
					false);

			if (pbus != null) {
				for (PsnBankaccUnionVO vo : pbus) {
					if (vo.getBankaccbasVO() != null) {
						BankAccSubVO[] bankAccSubVO = vo.getBankaccbasVO()
								.getBankaccsub();

						if ((bankAccSubVO != null) && (bankAccSubVO.length > 0)
								&& (bankAccSubVO[0] != null)) {
							pkBankaccsubList.add(bankAccSubVO[0]
									.getPk_bankaccsub());
						}
					}
				}
			}
			IPsnBankaccPubService pa = (IPsnBankaccPubService) NCLocator
					.getInstance()
					.lookup(IPsnBankaccPubService.class.getName());
			BankAccbasVO bankAccbasVO = pa
					.queryDefaultBankAccByPsnDoc(receiver);
			if (bankAccbasVO != null) {
				Integer enAbleState = bankAccbasVO.getEnablestate();
				BankAccSubVO[] bankAccSubVO = bankAccbasVO.getBankaccsub();
				if ((bankAccSubVO != null) && (bankAccSubVO.length > 0)
						&& (bankAccSubVO[0] != null)) {
					if ((pkBankaccsubList.contains(bankAccSubVO[0]
							.getPk_bankaccsub()))
							&& (enAbleState.intValue() == 2))
						return bankAccSubVO[0].getPk_bankaccsub();
				}
			}
		} catch (Exception e) {
			ExpUtil.setRowValue(row, busitemDs, "skyhzh", null);
			Logger.error(e.getMessage(), e);
		}
		return null;
	}

	private String getCustAcc(Row row, Dataset busitemDs, String tFlag) {
		if ("hbbm".equals(tFlag)) {
			String hbbm = (String) row.getValue(busitemDs.nameToIndex("hbbm"));
			ISupplierPubService service = (ISupplierPubService) NCLocator
					.getInstance().lookup(ISupplierPubService.class.getName());
			try {
				return service.getDefaultBankAcc(hbbm);
			} catch (Exception ex) {
				Log.getInstance(getClass()).error(ex);
			}
		}
		if ("customer".equals(tFlag)) {
			String customer = (String) row.getValue(busitemDs
					.nameToIndex("customer"));
			ICustomerPubService service = (ICustomerPubService) NCLocator
					.getInstance().lookup(ICustomerPubService.class.getName());
			try {
				return service.getDefaultBankAcc(customer);
			} catch (Exception ex) {
				Log.getInstance(getClass()).error(ex);
			}
		}
		return null;
	}

	public void doContract(Dataset busitemDs, Row busitemRow)
			throws ValidationException, BusinessException {
		UFDouble ybje = busitemRow.getUFDobule(busitemDs.nameToIndex("ybje"));

		LfwWindow pageMeta = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		LfwView widget = pageMeta.getWidget("main");

		Dataset masterDs = widget.getViewModels().getDataset(this.masterDsID);
		Row bxzbRow = masterDs.getSelectedRow();
		if (bxzbRow == null) {
			return;
		}
		String djdl = (String) bxzbRow.getValue(masterDs.nameToIndex("djdl"));

		if ((ybje != null) && (!ybje.equals(UFDouble.ZERO_DBL))
				&& ("bx".equals(djdl))) {
			Dataset contractDs = widget.getViewModels().getDataset("contrast");
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			SuperVO[] pvos = ser.serialize(contractDs);
			if ((pvos != null) && (pvos.length > 0)) {
				List contrastsList = new ArrayList();
				for (int i = 0; i < pvos.length; i++) {
					contrastsList.add((BxcontrastVO) pvos[i]);
				}

				new CjkMainViewCtrl().doContrast(masterDs, bxzbRow,
						contrastsList);
			}
		}
	}

	public void modifyFinValues(int jeIndex, int rowIndex, Dataset busitemDs,
			Row row) {
		UFDouble ybje = row.getValue(busitemDs.nameToIndex("ybje")) == null ? new UFDouble(
				0) : (UFDouble) row.getValue(busitemDs.nameToIndex("ybje"));

		UFDouble cjkybje = row.getValue(busitemDs.nameToIndex("cjkybje")) == null ? new UFDouble(
				0) : (UFDouble) row.getValue(busitemDs.nameToIndex("cjkybje"));

		UFDouble zfybje = row.getValue(busitemDs.nameToIndex("zfybje")) == null ? new UFDouble(
				0) : (UFDouble) row.getValue(busitemDs.nameToIndex("zfybje"));

		UFDouble hkybje = row.getValue(busitemDs.nameToIndex("hkybje")) == null ? new UFDouble(
				0) : (UFDouble) row.getValue(busitemDs.nameToIndex("hkybje"));

		if ((busitemDs.nameToIndex("ybje") == jeIndex)
				|| (busitemDs.nameToIndex("cjkybje") == jeIndex)) {
			if (ybje.getDouble() > cjkybje.getDouble()) {
				ExpUtil.setRowValue(row, busitemDs, "zfybje", ybje.sub(cjkybje));
				ExpUtil.setRowValue(row, busitemDs, "hkybje", new UFDouble(0));
				ExpUtil.setRowValue(row, busitemDs, "cjkybje", cjkybje);
			} else {
				ExpUtil.setRowValue(row, busitemDs, "hkybje", cjkybje.sub(ybje));
				ExpUtil.setRowValue(row, busitemDs, "zfybje", new UFDouble(0));
				ExpUtil.setRowValue(row, busitemDs, "cjkybje", cjkybje);
			}

		}

		ExpUtil.setRowValue(row, busitemDs, "ybye", ybje);

		transFinYbjeToBbje(rowIndex, busitemDs, row);
	}

	protected void transFinYbjeToBbje(int rowIndex, Dataset busitemDs, Row row) {
		UFDouble ybje = (UFDouble) row.getValue(busitemDs.nameToIndex("ybje"));
		UFDouble cjkybje = (UFDouble) row.getValue(busitemDs
				.nameToIndex("cjkybje"));
		UFDouble hkybje = (UFDouble) row.getValue(busitemDs
				.nameToIndex("hkybje"));
		UFDouble zfybje = (UFDouble) row.getValue(busitemDs
				.nameToIndex("zfybje"));

		LfwWindow jkbxWindow = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		LfwView widget = jkbxWindow.getView("main");
		Dataset masterDs = widget.getViewModels().getDataset(this.masterDsID);
		Row rowHead = masterDs.getCurrentRowData().getSelectedRow();

		String bzbm = (String) rowHead.getValue(masterDs.nameToIndex("bzbm"));
		if (("".equals(bzbm)) || (bzbm == null)) {
			bzbm = "null";
		}

		UFDouble hl = (UFDouble) rowHead.getValue(masterDs.nameToIndex("bbhl"));
		UFDouble grouphl = (UFDouble) rowHead.getValue(masterDs
				.nameToIndex("groupbbhl"));
		UFDouble globalhl = (UFDouble) rowHead.getValue(masterDs
				.nameToIndex("globalbbhl"));

		String pk_org = (String) rowHead.getValue(masterDs
				.nameToIndex("pk_org"));
		String pk_group = ExpUtil.getPKGroup();
		try {
			UFDouble[] bbje = Currency.computeYFB(pk_org, 1, bzbm, ybje, null,
					null, null, hl, ExpUtil.getSysdate());

			ExpUtil.setRowValue(row, busitemDs, "bbje", bbje[2]);
			ExpUtil.setRowValue(row, busitemDs, "bbye", bbje[2]);

			bbje = Currency.computeYFB(pk_org, 1, bzbm, cjkybje, null, null,
					null, hl, ExpUtil.getSysdate());

			ExpUtil.setRowValue(row, busitemDs, "cjkbbje", bbje[2]);

			bbje = Currency.computeYFB(pk_org, 1, bzbm, hkybje, null, null,
					null, hl, ExpUtil.getSysdate());

			ExpUtil.setRowValue(row, busitemDs, "hkbbje", bbje[2]);

			bbje = Currency.computeYFB(pk_org, 1, bzbm, zfybje, null, null,
					null, hl, ExpUtil.getSysdate());

			ExpUtil.setRowValue(row, busitemDs, "zfbbje", bbje[2]);

			UFDouble[] je = Currency.computeYFB(pk_org, 1, bzbm, ybje, null,
					null, null, hl, ExpUtil.getSysdate());

			UFDouble[] money = Currency.computeGroupGlobalAmount(je[0], je[2],
					bzbm, ExpUtil.getSysdate(), pk_org, pk_group, globalhl,
					grouphl);

			ExpUtil.setRowValue(row, busitemDs, "groupbbje", money[0]);
			ExpUtil.setRowValue(row, busitemDs, "globalbbje", money[1]);
		} catch (BusinessException e) {
			ExceptionHandler.consume(e);
		}
	}

	public void setHeadTotalValue() {
		LfwWindow jkbxWindow = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		LfwView widget = jkbxWindow.getView("main");

		Dataset[] allDs = widget.getViewModels().getDatasets();
		List datasetList = new ArrayList();

		String totalItem = "amount";

		if (ExpUtil.isFyadjust()) {
			datasetList.add(widget.getViewModels().getDataset(
					"bx_cshare_detail"));
			totalItem = "assume_amount";
		} else {
			for (int i = 0; i < allDs.length; i++) {
				String dsID = allDs[i].getId();
				if ((!dsID.startsWith("$refds")) && (!dsID.equals("bxzb"))
						&& (!dsID.equals("jkzb")) && (!dsID.equals("contrast"))
						&& (!dsID.equals("finitem"))
						&& (!dsID.equals("jkcontrast"))
						&& (!dsID.equals("jkfinitem"))
						&& (!dsID.equals("bx_cshare_detail"))
						&& (!dsID.equals("bx_accrued_verify"))
						&& (!dsID.equals("bx_tbbdetail"))) {
					datasetList.add(allDs[i]);
				}
			}
		}

		Row row = null;
		UFDouble totalAll = new UFDouble(0);
		for (int i = 0; i < datasetList.size(); i++) {
			Dataset thisDs = (Dataset) datasetList.get(i);

			RowData rowData = thisDs.getCurrentRowData();
			if (rowData != null) {
				Row[] rowArr = rowData.getRows();
				for (int j = 0; j < rowArr.length; j++) {
					row = rowArr[j];
					int index = thisDs.nameToIndex(totalItem);
					UFDouble amount = (UFDouble) row.getValue(index);

					if (amount != null) {
						totalAll = totalAll.add(amount);
					}
				}
			}

		}

		Dataset masterDs = widget.getViewModels().getDataset(this.masterDsID);
		Row row_bxzb = masterDs.getCurrentRowData().getSelectedRow();

		ExpUtil.setRowValue(row_bxzb, masterDs, "total", totalAll);
		ExpUtil.setRowValue(row_bxzb, masterDs, "ybje", totalAll);
	}

	public void setHeadYbjeValue() {
		LfwWindow jkbxWindow = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		LfwView widget = jkbxWindow.getView("main");

		Dataset[] allDs = widget.getViewModels().getDatasets();
		List datasetList = new ArrayList();

		for (int i = 0; i < allDs.length; i++) {
			String dsID = allDs[i].getId();
			if ((!dsID.startsWith("$refds")) && (!dsID.equals("bxzb"))
					&& (!dsID.equals("jkzb")) && (!dsID.equals("contrast"))
					&& (!dsID.equals("finitem"))
					&& (!dsID.equals("jkcontrast"))
					&& (!dsID.equals("jkfinitem"))
					&& (!dsID.equals("bx_cshare_detail"))
					&& (!dsID.equals("bx_accrued_verify"))
					&& (!dsID.equals("bx_tbbdetail"))) {
				datasetList.add(allDs[i]);
			}
		}
		Row row = null;
		UFDouble totalAll = new UFDouble(0);
		for (int i = 0; i < datasetList.size(); i++) {
			Dataset thisDs = (Dataset) datasetList.get(i);

			RowData rowData = thisDs.getCurrentRowData();
			if (rowData != null) {
				Row[] rowArr = rowData.getRows();
				for (int j = 0; j < rowArr.length; j++) {
					row = rowArr[j];
					int index = thisDs.nameToIndex("ybje");
					UFDouble amount = (UFDouble) row.getValue(index);
					if (amount != null) {
						totalAll = totalAll.add(amount);
					}
				}
			}

		}

		Dataset masterDs = widget.getViewModels().getDataset(this.masterDsID);
		Row row_bxzb = masterDs.getCurrentRowData().getSelectedRow();

		ExpUtil.setRowValue(row_bxzb, masterDs, "ybje", totalAll);
	}

	private void processEditorFormular(Dataset ds) {
		RowData rd = ds.getCurrentRowData();
		if (rd == null)
			return;
		Row selectedRow = ds.getSelectedRow();
		if (selectedRow == null)
			return;
		List executedFpList = new ArrayList();
		int fieldCount = ds.getFieldSet().getFieldCount();
		FormulaParse fp = LfwFormulaParser.getInstance();
		for (int i = 0; i < fieldCount; i++)
			try {
				Field field = ds.getFieldSet().getField(i);
				String formular = field.getEditFormular();
				if (formular != null) {
					if (!executedFpList.contains(formular)) {
						executedFpList.add(formular);
						String[] expArr = formular.split(";");
						fp.setExpressArray(expArr);
						VarryVO[] varryVOs = fp.getVarryArray();
						if ((varryVOs != null) && (varryVOs.length > 0)) {
							String[] formularNames = new String[varryVOs.length];

							Map indexMap = getIndexMap(ds);
							for (int j = 0; j < varryVOs.length; j++) {
								String[] keys = varryVOs[j].getVarry();
								if (keys != null) {
									for (String key : keys) {
										List valueList = new ArrayList();

										if (indexMap.get(key) != null) {
											Object value = selectedRow
													.getValue(((Integer) indexMap
															.get(key))
															.intValue());
											if (field.getExtendAttribute(field
													.getId()) != null) {
												String refKey = ((Field) field
														.getExtendAttributeValue(field
																.getId()))
														.getId();
												value = selectedRow
														.getValue(((Integer) indexMap
																.get(refKey))
																.intValue());
											}

											Field f = ds.getFieldSet()
													.getField(key);
											if ((f != null) && (value != null)) {
												if (("UFDouble".equals(f
														.getDataType()))
														|| ("Double".equals(f
																.getDataType()))
														|| ("Decimal".equals(f
																.getDataType()))
														|| (("SelfDefine"
																.equals(f
																		.getDataType()))
																&& (f.getPrecision() != null) && (!f
																.getPrecision()
																.equals("")))) {
													if (!(value instanceof UFDouble))
														value = new UFDouble(
																value.toString());
												} else if (("Integer".equals(f
														.getDataType()))
														&& (!(value instanceof Integer))) {
													value = new Integer(
															(String) value);
												}
											}
											valueList.add(value);

											fp.addVariable(key, valueList);
										}
									}
									formularNames[j] = varryVOs[j]
											.getFormulaName();
								}
							}
							Object[][] result = fp.getValueOArray();
							if (result != null)
								for (int l = 0; l < formularNames.length; l++) {
									int index = ds
											.nameToIndex(formularNames[l]);
									if (index == -1) {
										LfwLogger.error("can not find column:"
												+ formularNames[l] + ", ds id:"
												+ ds.getId());
									} else
										selectedRow.setValue(index,
												result[l][0]);
								}
						} else {
							fp.getValueOArray();
						}
					}
				}
			} catch (Throwable e) {
				if ((e instanceof LfwRuntimeException))
					throw ((LfwRuntimeException) e);
				Logger.error(e.getMessage(), e);
			}
	}

	private Map<String, Integer> getIndexMap(Dataset ds) {
		Map indexMap = new HashMap();
		int count = ds.getFieldSet().getFieldCount();
		for (int i = 0; i < count; i++) {
			Field field = ds.getFieldSet().getField(i);
			String key = field.getId();
			indexMap.put(key, Integer.valueOf(i));
		}
		return indexMap;
	}

	public void dealReimRuleTips(int index) {
		LfwWindow jkbxWindow = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		String expreimruleInteractionFlag = LfwRuntimeEnvironment
				.getWebContext().getRequest()
				.getParameter("expreimruleinteractflag");
		LfwView widget = jkbxWindow.getView("main");
		Dataset masterDs = widget.getViewModels().getDataset(this.masterDsID);
		Row headSelRow = masterDs.getSelectedRow();
		String[] assignPks = (String[]) AppUtil.getAppAttr("ncAssginUsers");
		String bxdj = (String) AppUtil.getAppAttr("exp_reimrule_flag");
		Dataset[] detailDss = ExpUtil.getAllDetailDss(widget, this.masterDsID);
		Datasets2AggVOSerializer serializer = new ExpDatasets2AggVOSerializer();
		AggregatedValueObject aggVo = serializer.serialize(masterDs, detailDss,
				BXVO.class.getName());
		BXVO jkbxvo = (BXVO) aggVo;
		if ((bxdj != null) && ("Y".equals(bxdj))) {
			List<BodyEditVO> result = YerBxUIControlUtil.doBodyReimAction(jkbxvo,
					ExpReimruleUtil.getReimRuleDataMap(masterDs, headSelRow),
					ExpUtil.getBusitemDss(widget, this.masterDsID),
					ExpReimruleUtil.getReimDimMap(masterDs, headSelRow));
			Dataset[] busitems = ExpUtil.getBusitemDss(widget, this.masterDsID);
			if ((expreimruleInteractionFlag == null) && (assignPks == null)) {
				String warnMessage = "";
				String errorMessage = "";
				String preMessage1 = LfwResBundle.getInstance().getStrByID(
						"weberm", "ExpUifCommitCmd-000002");
				String preMessage2 = LfwResBundle.getInstance().getStrByID(
						"weberm", "ExpUifCommitCmd-000003");
				String preMessage3 = LfwResBundle.getInstance().getStrByID(
						"weberm", "ExpUifCommitCmd-000004");
				String preMessage4 = LfwResBundle.getInstance().getStrByID(
						"weberm", "ExpUifCommitCmd-000005");
				String preMessage5 = LfwResBundle.getInstance().getStrByID(
						"weberm", "ExpUifCommitCmd-000006");
				String preMessage6 = LfwResBundle.getInstance().getStrByID(
						"weberm", "ExpUifCommitCmd-000007");
				int tip = 0;
				UFDouble stdVlu = null;
				int JD = -1;
				for (BodyEditVO vo : result) {
					if (((vo instanceof ControlBodyEditVO))
							&& (vo.getRow() == index)) {
						tip = ((ControlBodyEditVO) vo).getTip();
						for (Dataset ds : busitems) {
							if (vo.getTablecode().equals(
									ds.getExtendAttributeValue("$TAB_CODE"))) {
								Object itemValue = ds
										.getCurrentRowData()
										.getRow(vo.getRow())
										.getValue(
												ds.nameToIndex(vo.getItemkey()));
								if (itemValue == null) {
									itemValue = UFDouble.ZERO_DBL;
								}
								UFDouble currentValue = new UFDouble(
										itemValue.toString());
								UFDouble standardValue = new UFDouble(
										UFDouble.ZERO_DBL);
								if (((ControlBodyEditVO) vo).getFormulaRule() != null) {
									String formular = vo.getItemkey()
											+ "->"
											+ ((ControlBodyEditVO) vo)
													.getFormulaRule();
									Object newValue = YerBxUIControlUtil
											.execEditorFormular(ds, ds
													.getCurrentRowData()
													.getRow(vo.getRow()), vo
													.getItemkey(), formular);
									if (newValue != null)
										standardValue = new UFDouble(
												newValue.toString());
								} else {
									standardValue = new UFDouble(vo.getValue()
											.toString());
								}

								String fieldName = getFieldName(widget,
										vo.getItemkey());
								if (currentValue.compareTo(standardValue) == 1) {
									if (1 == tip) {
										warnMessage = warnMessage + preMessage2
												+ "【" + (vo.getRow() + 1) + "】"
												+ preMessage3 + "【" + fieldName
												+ "】" + ",";
									} else if (2 == tip) {
										stdVlu = standardValue;
										JD = ds.getFieldSet()
												.getField(vo.getItemkey())
												.getPrecision() == null ? -1
												: Integer
														.parseInt(ds
																.getFieldSet()
																.getField(
																		vo.getItemkey())
																.getPrecision());

										if (JD != -1) {
											stdVlu = new UFDouble(
													stdVlu.doubleValue(), JD);
										}
										errorMessage = errorMessage
												+ preMessage2 + "【"
												+ (vo.getRow() + 1) + "】"
												+ preMessage3 + "【" + fieldName
												+ "】" + preMessage4 + ":"
												+ stdVlu + ".";
									}
								}
							}
						}
					}
				}
				if (errorMessage.length() > 0) {
					errorMessage = preMessage1
							+ ":"
							+ errorMessage.substring(0,
									errorMessage.length() - 1) + preMessage6;
					AppInteractionUtil.showMessageDialog(errorMessage);
				}
				if (warnMessage.length() > 0) {
					warnMessage = preMessage1
							+ ":"
							+ warnMessage
									.substring(0, warnMessage.length() - 1)
							+ preMessage4;
					AppInteractionUtil.showMessageDialog(warnMessage);
				}
			}
		}
	}

	private String getFieldName(LfwView mainWidget, String itemkey) {
		String fieldName = "";
		WebComponent[] components = mainWidget.getViewComponents()
				.getComponents();
		if (components != null) {
			for (WebComponent com : components) {
				if (((com instanceof GridComp))
						&& ("busitem_grid".equals(com.getId()))) {
					for (IGridColumn column : ((GridComp) com).getColumnList()) {
						GridColumn thisColumn = (GridColumn) column;
						if (thisColumn.getField().equals(itemkey)) {
							fieldName = thisColumn.getText();
							break;
						}
					}
				}
			}
		}

		return fieldName;
	}
}