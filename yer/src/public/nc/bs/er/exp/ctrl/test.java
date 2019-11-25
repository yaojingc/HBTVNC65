package nc.bs.er.exp.ctrl;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import nc.bs.er.exp.cmd.ExpBodyValueChangeCmd;
import nc.bs.er.exp.cmd.ExpCopyCmd;
import nc.bs.er.exp.cmd.ExpLinkNtbCmd;
import nc.bs.er.exp.cmd.ExpUifAddCmd;
import nc.bs.er.exp.cmd.ExpUifCancelCmd;
import nc.bs.er.exp.cmd.ExpUifCommitCmd;
import nc.bs.er.exp.cmd.ExpUifDeleteCmd;
import nc.bs.er.exp.cmd.ExpUifEditCmd;
import nc.bs.er.exp.cmd.ExpUifTempSaveCmd;
import nc.bs.er.exp.fysq.ctrl.ExpAdvancedQueryController;
import nc.bs.er.exp.model.ExpFromFysqQueryPageModel;
import nc.bs.er.exp.print.ExpPrintImpl;
import nc.bs.er.linkpfinfo.PfinfoPageModel;
import nc.bs.er.util.YerUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.ctrl.excel.UifExcelImportCmd;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.ICpFreeFormTemplatePrintService;
import nc.uap.ctrl.tpl.qry.CpQueryTemplatePageModel;
import nc.uap.lfw.core.AppSession;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewModels;
import nc.vo.arap.bx.util.BXUtil;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.BusiTypeVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDouble;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uap.web.bd.pub.AppUtil;

/**
 * ≤‚ ‘gitÃ·Ωª
 * 
 * @author yao
 * 
 */
public class test implements IController {
	public void add(MouseEvent mouseEvent) {
		String formID = "bxzb_base_info_form";
		if ("jkzb".equals(getMasterDsId())) {
			formID = "jkzb_base_info_form";
		}
		LfwView widget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) widget.getViewComponents().getComponent(
				formID);
		if (6 == form.getRenderType()) {
			String url = (String) AppUtil.getAppAttr("DJURL");
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.sendRedirect(
							LfwRuntimeEnvironment.getRootPath() + "/" + url);
		} else {
			UifCommand addCmd = new ExpUifAddCmd(getMasterDsId());
			addCmd.execute();
		}
	}

	public void addFromFysq(MouseEvent mouseEvent) {
		String formID = "bxzb_base_info_form";
		if ("jkzb".equals(getMasterDsId())) {
			formID = "jkzb_base_info_form";
		}
		LfwView widget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) widget.getViewComponents().getComponent(
				formID);

		if (6 == form.getRenderType()) {
			String url = (String) AppUtil.getAppAttr("DJURL");
			url = url + "&isReadonlyFromFysq=Y";
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.sendRedirect(
							LfwRuntimeEnvironment.getRootPath() + "/" + url);
		} else {
			add(mouseEvent);

			Map queryMap = new HashMap();

			String queryNode = "E1100301";
			String nodekey = (String) AppLifeCycleContext.current()
					.getApplicationContext().getAppAttribute("$QueryTemplate");

			queryMap.put("$QueryTemplate", queryNode);
			queryMap.put("nodekey", nodekey);
			queryMap.put("model", ExpFromFysqQueryPageModel.class.getName());

			queryMap.put("clc", ExpAdvancedQueryController.class.getName());
			LfwWindow jkbxWindow = AppLifeCycleContext.current()
					.getWindowContext().getWindow();
			Dataset jkbxHeadDs = jkbxWindow.getView("main").getViewModels()
					.getDataset(getMasterDsId());
			nc.uap.lfw.core.data.Row row = jkbxHeadDs.getSelectedRow();
			String pk_org = (String) row.getValue(jkbxHeadDs
					.nameToIndex("pk_org"));
			String bzbm = (String) row.getValue(jkbxHeadDs.nameToIndex("bzbm"));
			String djlxbm = (String) row.getValue(jkbxHeadDs
					.nameToIndex("djlxbm"));
			AppUtil.addAppAttr("addfromfysq_org", pk_org);
			AppUtil.addAppAttr("addfromfysq_bzbm", bzbm);
			AppUtil.addAppAttr("addfromfysq_djlxbm", djlxbm);

			OpenProperties props = new OpenProperties();
			props.setWidth("900");
			props.setHeight("700");
			props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"er_nodes", "0E110YER-WEBERM-CODE-00001"));

			props.setOpenId("uap.lfw.imp.query.advquery");
			props.setParamMap(queryMap);
			AppLifeCycleContext.current().getViewContext().navgateTo(props);
		}
	}

	public void conditionQueryPlugin(Map keys) {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		ViewContext viewCtx = ctx.getWindowContext().getCurrentViewContext();
		CmdInvoker.invoke(new UifPlugoutCmd(viewCtx.getId(), "MenuBXPlugOut",
				keys));
	}

	public void edit(MouseEvent mouseEvent) {
		UifCommand editCmd = new ExpUifEditCmd(getMasterDsId());
		editCmd.execute();
	}

	public void delete(MouseEvent mouseEvent) {
		String billType = (String) AppUtil.getAppAttr("$$$$$$$$FLOWTYPEPK");

		UifCommand deleteCmd = new ExpUifDeleteCmd("main", getMasterDsId(),
				billType);
		deleteCmd.execute();
	}

	public void commit(MouseEvent mouseEvent) {
		UifCommand commitCmd = new ExpUifCommitCmd(getMasterDsId(),
				getDetailDsIds(), BXVO.class.getName(), getBodyNotNull());

		commitCmd.execute();

		AppUtil.getCntAppCtx().addExecScript(
				";try{getTrueParent().reloadData();}catch(e){}");
	}

	public void tempSave(MouseEvent mouseEvent) {
		UifCommand tempSaveCmd = new ExpUifTempSaveCmd(getMasterDsId(),
				getDetailDsIds(), BXVO.class.getName(), getBodyNotNull());

		tempSaveCmd.execute();
	}

	public void copy(MouseEvent mouseEvent) {
		String formID = "bxzb_base_info_form";
		if ("jkzb".equals(getMasterDsId())) {
			formID = "jkzb_base_info_form";
		}
		LfwView widget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) widget.getViewComponents().getComponent(
				formID);
		if (6 == form.getRenderType()) {
			Dataset masterDs = widget.getViewModels().getDataset(
					getMasterDsId());
			nc.uap.lfw.core.data.Row row = masterDs.getSelectedRow();
			if (row == null) {
				return;
			}

			String pk_bill = (String) row.getValue(masterDs
					.nameToIndex("pk_jkbx"));
			int djzt = ((Integer) row.getValue(masterDs.nameToIndex("djzt")))
					.intValue();
			String url = (String) AppUtil.getAppAttr("DJURL");
			url = url + "&taskPk=ncerwfmtaskqry";
			url = url + "&openBillId=" + pk_bill;

			url = url + "&NC=Y";
			url = url + "&isCopyOpen=Y";
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.sendRedirect(
							LfwRuntimeEnvironment.getRootPath() + "/" + url);
		} else {
			UifCommand copyCmd = new ExpCopyCmd(getMasterDsId(), "main");
			copyCmd.execute();
		}
	}

	public void cancel(MouseEvent mouseEvent) {
		JKBXVO jkbxVO = (JKBXVO) AppUtil.getAppAttr("COPY_CANCEL_JKBXVO");
		int djzt = jkbxVO.getParentVO().getDjzt().intValue();
		if ((djzt != 0) && (djzt != 1)) {
			String pk_bill = jkbxVO.getParentVO().getPk_jkbx();
			String url = (String) AppUtil.getAppAttr("DJURL");
			url = url + "&taskPk=ncerwfmtaskqry";
			url = url + "&openBillId=" + pk_bill;
			url = url + "&djzt=" + djzt;
			url = url + "&NC=Y";
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.sendRedirect(
							LfwRuntimeEnvironment.getRootPath() + "/" + url);
		} else {
			UifCommand cancelCmd = new ExpUifCancelCmd(getMasterDsId());
			cancelCmd.execute();
		}
	}

	public void cjk(MouseEvent mouseEvent) {
		LfwWindow bxWindow = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		Dataset bxheadDs = bxWindow.getView("main").getViewModels()
				.getDataset("bxzb");
		nc.uap.lfw.core.data.Row headRow = bxheadDs.getSelectedRow();
		String djlxbm = (String) headRow.getValue(bxheadDs
				.nameToIndex("djlxbm"));

		if (!"2647".equals(djlxbm)) {
			UFDouble totalAmount = (UFDouble) headRow.getValue(bxheadDs
					.nameToIndex("total"));
			if (totalAmount.compareTo(UFDouble.ZERO_DBL) == 0)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("per_codes", "0per_codes0411"));
			if (totalAmount.compareTo(UFDouble.ZERO_DBL) < 0) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("per_codes", "0per_codes0418"));
			}
			Dataset busitemDs = bxWindow.getView("main").getViewModels()
					.getDataset("busitem");
			if (busitemDs.getCurrentRowData() != null) {
				nc.uap.lfw.core.data.Row[] row = busitemDs.getCurrentRowData()
						.getRows();
				if (row != null) {
					for (int i = 0; i < row.length; i++) {
						UFDouble busitemAmount = (UFDouble) row[i]
								.getValue(busitemDs.nameToIndex("amount"));
						if ((busitemAmount == null)
								|| (busitemAmount.compareTo(UFDouble.ZERO_DBL) <= 0)) {
							throw new LfwRuntimeException(NCLangRes4VoTransl
									.getNCLangRes().getStrByID("per_codes",
											"0per_codes0421"));
						}
					}
				}
			}
		}
		AppLifeCycleContext
				.current()
				.getWindowContext()
				.popView(
						"cjk",
						"850",
						"450",
						NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"per_codes", "0per_codes0014"), false);
	}

	public void quickShare(MouseEvent mouseEvent) {
		LfwWindow bxWindow = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		Dataset bxheadDs = bxWindow.getView("main").getViewModels()
				.getDataset("bxzb");
		nc.uap.lfw.core.data.Row headRow = bxheadDs.getSelectedRow();

		UFDouble totalAmount = (UFDouble) headRow.getValue(bxheadDs
				.nameToIndex("total"));

		if (totalAmount.compareTo(UFDouble.ZERO_DBL) == 0)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("201107_0", "0201107-0003"));
		if (totalAmount.compareTo(UFDouble.ZERO_DBL) < 0) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("per_codes", "0per_codes0419"));
		}
		Dataset cShareDs = bxWindow.getView("main").getViewModels()
				.getDataset("bx_cshare_detail");
		if (cShareDs.getSelectedRow() != null) {
			UFDouble shareAmount = (UFDouble) cShareDs.getSelectedRow()
					.getValue(cShareDs.nameToIndex("assume_amount"));
			if (shareAmount == null)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("per_codes", "0per_codes0416"));
			if (shareAmount.compareTo(UFDouble.ZERO_DBL) == 0)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("201107_0", "0201107-0003"));
			if (shareAmount.compareTo(UFDouble.ZERO_DBL) < 0) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("per_codes", "0per_codes0419"));
			}

		}

		AppLifeCycleContext
				.current()
				.getWindowContext()
				.popView(
						"quickshare",
						"600",
						"315",
						NCLangRes4VoTransl.getNCLangRes().getStrByID("yer",
								"0E110YER-V63-004"), false);
	}

	public void query(MouseEvent mouseEvent) {
		Map queryMap = new HashMap();
		queryMap.put("model", CpQueryTemplatePageModel.class.getName());
		String queryNode = (String) AppLifeCycleContext.current()
				.getApplicationContext().getAppAttribute("nodecode");
		if (queryNode != null)
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.addOriginalParameter("nodecode", queryNode);
		else
			queryNode = LfwRuntimeEnvironment.getWebContext().getAppSession()
					.getOriginalParameter("nodecode");
		queryMap.put("$QueryTemplate", queryNode);
		String appId = (String) LfwRuntimeEnvironment.getWebContext()
				.getAppSession().getAttribute("appId");
		String url = LfwRuntimeEnvironment.getRootPath() + "/app/" + appId
				+ "/queryTemplate?";
		String appUniqueId = LfwRuntimeEnvironment.getWebContext()
				.getAppUniqueId();
		url = url + "appUniqueId=" + appUniqueId;

		if (queryMap != null) {
			Iterator entryIt = queryMap.entrySet().iterator();
			while (entryIt.hasNext()) {
				Map.Entry entry = (Map.Entry) entryIt.next();
				url = url + "&" + (String) entry.getKey() + "="
						+ (String) entry.getValue();
			}
		}
		url = url + "&isAdvanceQuery=true";

		String otherPageId = AppLifeCycleContext.current()
				.getApplicationContext().getCurrentWindowContext().getId();
		String widgetId = AppLifeCycleContext.current().getApplicationContext()
				.getCurrentWindowContext().getCurrentViewContext().getId();
		url = url + "&otherPageId=" + otherPageId;
		url = url + "&widgetId=" + widgetId;
		StringBuffer buf = new StringBuffer();
		AppLifeCycleContext
				.current()
				.getApplicationContext()
				.addExecScript(
						"showDialog(\""
								+ url
								+ "\", \""
								+ NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"uapquerytemplate",
										"SimpleQueryController-000000")
								+ "\", " + "800" + "," + "600" + ", \""
								+ "queryTemplate" + "\",   true  ,  true );");
	}

	public void excelImport(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset mailds = mainWidget.getViewModels().getDataset("bxzb");
		String djlxbm = mailds.getSelectedRow().getString(
				mailds.nameToIndex("djlxbm"));
		if ("264X-Cxx-03".equals(djlxbm)) {
			UifExcelImportCmd cmd = new UifExcelImportCmd(getClass().getName(),
					"viewid");
			CmdInvoker.invoke(cmd);
		}
	}

	public void onUploadedExcelFile(ScriptEvent e) {
		String filePath = ContextResourceUtil.getCurrentAppPath()
				+ AppLifeCycleContext.current().getParameter("excel_imp_path");
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(filePath));
		} catch (Exception ex) {
			try {
				workbook = new HSSFWorkbook(new FileInputStream(filePath));
			} catch (Exception ex1) {
				throw new IllegalArgumentException("ExcelÊñá‰ª∂ËØªÂèñÂ§±Ë¥•!");
			}
		}
		Sheet sheet = workbook.getSheet("Sheet1");
		int rowTotal = sheet.getPhysicalNumberOfRows();
		List list = new ArrayList();
		for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
			org.apache.poi.ss.usermodel.Row row = sheet.getRow(r);
			if (row != null) {
				Object[] cells = new Object[row.getLastCellNum()];
				for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell != null)
						cells[c] = getValueFromCell(cell);
				}
				list.add(cells);
			}
		}
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = mainWidget.getViewModels().getDataset("busitem");
		Dataset mailds = mainWidget.getViewModels().getDataset("bxzb");
		String pk_org = mailds.getSelectedRow().getString(
				mailds.nameToIndex("pk_org"));
		int rowsCount = ds.getRowCount();
		if (rowsCount == 1) {
			nc.uap.lfw.core.data.Row[] rows = ds.getAllRow();
			if (rows[0].getString(ds.nameToIndex("szxmid")) == null)
				ds.clear();
		}
		nc.uap.lfw.core.data.Row emptyRow = null;
		for (int i = 1; i < list.size(); i++) {
			try {
				Object[] l = (Object[]) list.get(i);
				emptyRow = ds.getEmptyRow();
				ds.addRow(emptyRow);
				int index = ds.getRowIndex(emptyRow);
				ds.setRowSelectIndex(Integer.valueOf(index));
				Object obj = null;
				emptyRow.setValue(ds.nameToIndex("pk_pcorg"), pk_org);
				if ((l[2] != null) && (!"".equals(l[2].toString()))) {
					obj = getUapQry().executeQuery(
							"select pk_inoutbusiclass from bd_inoutbusiclass where name='"
									+ l[2].toString().trim() + "'",
							new ColumnProcessor());
					emptyRow.setValue(ds.nameToIndex("szxmid"), obj);
				}
				if ((l[3] != null) && (!"".equals(l[3].toString()))) {
					obj = getUapQry()
							.executeQuery(
									"select pk_defdoc from bd_defdoc where name='"
											+ l[3].toString().trim()
											+ "' and pk_defdoclist='1001A11000000007M47W'",
									new ColumnProcessor());
					emptyRow.setValue(ds.nameToIndex("defitem50"), obj);
				}
				emptyRow.setValue(ds.nameToIndex("defitem39"), l[4]);
				UFDouble je = null;
				try {
					je = new UFDouble(l[5].toString());
				} catch (Exception ex) {
					je = new UFDouble(0);
				}
				emptyRow.setValue(ds.nameToIndex("amount"), je);
				emptyRow.setValue(ds.nameToIndex("ybje"), je);
				ExpBodyValueChangeCmd expBodyValueChangeCmd = new ExpBodyValueChangeCmd(
						getMasterDsId(), null);
				expBodyValueChangeCmd.setHeadTotalValue();
				expBodyValueChangeCmd.modifyFinValues(ds.nameToIndex("ybje"),
						index, ds, emptyRow);
				emptyRow.setValue(ds.nameToIndex("defitem42"), l[6]);
				emptyRow.setValue(ds.nameToIndex("defitem49"), l[7]);
				emptyRow.setValue(ds.nameToIndex("defitem48"), l[8]);
				emptyRow.setValue(ds.nameToIndex("defitem43"), l[9]);
				emptyRow.setValue(ds.nameToIndex("defitem44"), l[10]);
				if ((l[11] != null) && (!"".equals(l[11].toString()))) {
					obj = getUapQry().executeQuery(
							"select pk_banktype from bd_banktype where name='"
									+ l[11].toString().trim() + "'",
							new ColumnProcessor());
					emptyRow.setValue(ds.nameToIndex("defitem45"), obj);
				}
				emptyRow.setValue(ds.nameToIndex("defitem46"), l[12]);
				emptyRow.setValue(ds.nameToIndex("defitem47"), l[13]);
				emptyRow.setValue(ds.nameToIndex("defitem36"), l[14]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		ds.setEnabled(true);
	}

	private IUAPQueryBS getUapQry() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
	}

	private String getValueFromCell(Cell cell) {
		if (cell == null) {
			return null;
		}
		String value = null;
		switch (cell.getCellType()) {
		case 0:
			if (HSSFDateUtil.isCellDateFormatted(cell))
				value = new SimpleDateFormat("yyyy-MM-dd").format(cell
						.getDateCellValue());
			else {
				value = String.valueOf(
						new BigDecimal(cell.getNumericCellValue())).trim();
			}
			break;
		case 1:
			value = cell.getStringCellValue().trim();
			break;
		case 2:
			double numericValue = cell.getNumericCellValue();
			if (HSSFDateUtil.isValidExcelDate(numericValue))
				value = new SimpleDateFormat("yyyy-MM-dd").format(cell
						.getDateCellValue());
			else
				value = String.valueOf(new BigDecimal(numericValue));
			break;
		case 3:
			value = null;
			break;
		case 4:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case 5:
			value = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			value = null;
		}

		return value;
	}

	public void backToCenter(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = mainWidget.getViewModels().getDataset(getMasterDsId());
		nc.uap.lfw.core.data.Row row = ds.getSelectedRow();
		String billType = (String) row.getValue(ds.nameToIndex("djlxbm"));
		StringBuffer expCenterUrl = new StringBuffer(
				"app/app_expcenter?nodecode=E1100101");
		expCenterUrl.append("&").append("back_billtype").append("=")
				.append(billType);

		AppLifeCycleContext
				.current()
				.getApplicationContext()
				.addExecScript(
						"parent.setPortalNavData(\"<a>"
								+ NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"per_codes", "0per_codes0055")
								+ "</a><a>&nbsp;&nbsp;>&nbsp;&nbsp;</a><a>"
								+ NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"per_codes", "0per_codes0025")
								+ "</a><a>&nbsp;&nbsp;>&nbsp;&nbsp;</a><a>"
								+ NCLangRes4VoTransl.getNCLangRes().getStrByID(
										"per_codes", "0per_codes0412")
								+ "</a>\");");
		AppLifeCycleContext
				.current()
				.getApplicationContext()
				.sendRedirect(
						LfwRuntimeEnvironment.getRootPath() + "/"
								+ expCenterUrl);
	}

	public void print(MouseEvent mouseEvent) {
		String pk_jkbx = "";
		String djdl = "";
		String winId = "";

		pk_jkbx = AppLifeCycleContext.current().getParameter("pk_jkbx");
		djdl = AppLifeCycleContext.current().getParameter("djdl");

		String nodecode = AppLifeCycleContext.current()
				.getParameter("nodecode");
		if ((pk_jkbx == null) || ("".endsWith(pk_jkbx)) || (djdl == null)
				|| ("".equals(djdl))) {
			ViewContext viewContex = AppLifeCycleContext.current()
					.getWindowContext().getViewContext("main");
			AppLifeCycleContext.current().getWindowContext()
					.setCurrentViewContext(viewContex);
			LfwView print = viewContex.getView();
			Dataset ds = print.getViewModels().getDataset(getMasterDsId());

			nc.uap.lfw.core.data.Row row = ds.getSelectedRow();
			pk_jkbx = (String) row.getValue(ds.nameToIndex("pk_jkbx"));
			djdl = (String) row.getValue(ds.nameToIndex("djdl"));

			if ((pk_jkbx == null) || ("".endsWith(pk_jkbx)) || (djdl == null)
					|| ("".equals(djdl))) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "0E110YER-V63-005"));
			}

			if (nodecode == null) {
				nodecode = (String) LfwRuntimeEnvironment.getWebContext()
						.getAppSession().getAttribute("nodecode");
			}

			winId = AppLifeCycleContext.current().getWindowContext().getId();
		} else {
			winId = YerUtil.getColValue("cp_appsnode", "appid", "id", nodecode);
			if ((winId != null) && (!"".equals(winId))) {
				winId = winId.replace("app_", "");
			}
		}
		if (("".equals(winId)) && ((nodecode == null) || ("".equals(nodecode)))) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("yer", "0E010YER-0035"));
		}

		ICpPrintTemplateService printTemplate = (ICpPrintTemplateService) NCLocator
				.getInstance().lookup(ICpPrintTemplateService.class);
		try {
			ICpFreeFormTemplatePrintService expPrintServiceImpl = new ExpPrintImpl(
					new String[] { pk_jkbx }, djdl, winId, nodecode);
			if (nodecode != null)
				printTemplate.print(expPrintServiceImpl, null, nodecode);
			else
				printTemplate.print(expPrintServiceImpl);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}

	public void printCenter(ScriptEvent scriptEvent) {
		print(null);
	}

	public void onFileManagerClick(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = mainWidget.getViewModels().getDataset(getMasterDsId());
		nc.uap.lfw.core.data.Row row = ds.getSelectedRow();
		String pk_jkbx = (String) row.getValue(ds.nameToIndex("pk_jkbx"));
		if ((pk_jkbx == null) || ("".equals(pk_jkbx))) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("yer", "0E110YER-V63-006"));
		}
		AppUtil.addAppAttr("pk_jkbx", pk_jkbx);

		String spzt = String.valueOf(row.getValue(ds.nameToIndex("spzt")));
		AppUtil.addAppAttr("spzt", spzt);

		ApplicationContext appCtx = AppLifeCycleContext.current()
				.getApplicationContext();
		Map paraMap = new HashMap();
		String nodecode = (String) LfwRuntimeEnvironment.getWebContext()
				.getAppSession().getAttribute("nodecode");
		paraMap.put("nodecode", nodecode);
		appCtx.navgateTo("filemanager", NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("per_codes", "0per_codes0015"), "750", "450",
				paraMap);
	}

	public void linkPfinfo(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		String dsID = getMasterDsId();
		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
		nc.uap.lfw.core.data.Row currentRow = masterDs.getSelectedRow();
		if (currentRow != null) {
			String billtype = (String) currentRow.getValue(masterDs
					.nameToIndex("djlxbm"));
			String billid = (String) currentRow.getValue(masterDs
					.nameToIndex("pk_jkbx"));

			Map paramMap = new HashMap();

			paramMap.put("model", PfinfoPageModel.class.getName());
			paramMap.put("billType", billtype);
			paramMap.put("billId", billid);

			OpenProperties props = new OpenProperties();
			props.setWidth("1000");
			props.setHeight("600");
			props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"per_codes", "0per_codes0052"));
			props.setOpenId("pfinfo");
			props.setParamMap(paramMap);
			AppLifeCycleContext.current().getViewContext().navgateTo(props);
		}
	}

	public void linkYS(MouseEvent mouseEvent) {
		UifCommand linkNtbCmd = new ExpLinkNtbCmd(getMasterDsId(), "YS");
		linkNtbCmd.execute();
	}

	public void linkZJ(MouseEvent mouseEvent) {
		UifCommand linkNtbCmd = new ExpLinkNtbCmd(getMasterDsId(), "ZJ");
		linkNtbCmd.execute();
	}

	public void linkRule(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		String dsID = getMasterDsId();

		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
		nc.uap.lfw.core.data.Row currentRow = masterDs.getSelectedRow();
		if (currentRow != null) {
			String djlxbm = (String) currentRow.getValue(masterDs
					.nameToIndex("djlxbm"));
			String djdl = (String) currentRow.getValue(masterDs
					.nameToIndex("djdl"));
			BusiTypeVO busTypeVO = BXUtil.getBusTypeVO(djlxbm, djdl);

			String strUrl = busTypeVO.getRule();
			if ((strUrl == null) || (strUrl.trim().length() == 0)) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("expensepub_0", "02011002-0006"));
			}
			ApplicationContext ctx = AppLifeCycleContext.current()
					.getApplicationContext();
			ctx.popOuterWindow(strUrl, NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("weberm_0", "0E010001-0069"), "1000", "90%",
					"TYPE_WINDOW");
		}
	}

	public void linkLimit(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		String dsID = getMasterDsId();

		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
		nc.uap.lfw.core.data.Row currentRow = masterDs.getSelectedRow();
		if (currentRow != null) {
			String djlxbm = (String) currentRow.getValue(masterDs
					.nameToIndex("djlxbm"));
			String djdl = (String) currentRow.getValue(masterDs
					.nameToIndex("djdl"));
			BusiTypeVO busTypeVO = BXUtil.getBusTypeVO(djlxbm, djdl);

			String strUrl = busTypeVO.getLimit();
			if ((strUrl == null) || (strUrl.trim().length() == 0)) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("expensepub_0", "02011002-0008"));
			}
			ApplicationContext ctx = AppLifeCycleContext.current()
					.getApplicationContext();
			ctx.popOuterWindow(strUrl, NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("weberm_0", "0E010001-0068"), "1000", "90%",
					"TYPE_WINDOW");
		}
	}

	private void setPlutoutInvoke(String v) {
		Map p = new HashMap();
		p.put("menu_key", v);
		new UifPlugoutCmd("bx_menu", "MenuBXPlugOut", p).execute();
	}

	public void onclick(MouseEvent mouseEvent) {
	}

	protected String getMasterDsId() {
		return "bxzb";
	}

	private boolean getBodyNotNull() {
		return false;
	}

	private String[] getDetailDsIds() {
		String[] detailDsIds = null;

		LfwView widget = AppLifeCycleContext.current().getApplicationContext()
				.getCurrentWindowContext().getViewContext("main").getView();

		if (widget.getViewModels().getDsrelations() != null) {
			DatasetRelation[] rels = widget.getViewModels().getDsrelations()
					.getDsRelations(getMasterDsId());

			if (rels != null) {
				detailDsIds = new String[rels.length];
				for (int i = 0; i < rels.length; i++) {
					detailDsIds[i] = rels[i].getDetailDataset();
				}
			}
		}
		return detailDsIds;
	}
}