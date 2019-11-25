package nc.bs.er.exp.ctrl;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import nc.bs.er.exp.cmd.ExpBodyValueChangeCmd;
import nc.bs.er.exp.cmd.ExpCopyCmd;
import nc.bs.er.exp.cmd.ExpLinkNtbCmd;
import nc.bs.er.exp.cmd.ExpUifAddCmd;
import nc.bs.er.exp.cmd.ExpUifCancelCmd;
import nc.bs.er.exp.cmd.ExpUifCommitCmd;
import nc.bs.er.exp.cmd.ExpUifDeleteCmd;
import nc.bs.er.exp.cmd.ExpUifEditCmd;
import nc.bs.er.exp.cmd.ExpUifInvalidCmd;
import nc.bs.er.exp.cmd.ExpUifRecallCmd;
import nc.bs.er.exp.cmd.ExpUifSaveCmd;
import nc.bs.er.exp.cmd.ExpUifTempSaveCmd;
import nc.bs.er.exp.fysq.ctrl.ExpAdvancedQueryController;
import nc.bs.er.exp.model.ExpFromFysqQueryPageModel;
import nc.bs.er.exp.print.ExpPrintDataSource;
import nc.bs.er.exp.print.ExpPrintImpl;
import nc.bs.er.exp.util.ExpDatasets2AggVOSerializer;
import nc.bs.er.exp.util.ExpUtil;
import nc.bs.er.exp.util.YerPageUtil;
import nc.bs.er.linkbxd.model.LinkBXDPageModel;
import nc.bs.er.linkjkd.model.LinkJKDPageModel;
import nc.bs.er.linkpfinfo.PfinfoPageModel;
import nc.bs.er.linkytd.model.LinkYTDPageModel;
import nc.bs.er.util.YerCommonPrintCmd;
import nc.bs.er.util.YerUtil;
import nc.bs.er.workflow.YerWorkflowPfinfoPageModel;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.arap.prv.IBXBillPrivate;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.generator.IdGenerator;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.erm.matterapp.IErmMatterAppBillQuery;
import nc.pubitf.para.SysInitQuery;
import nc.uap.ctrl.excel.UifExcelImportCmd;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.ICpFreeFormTemplatePrintService;
import nc.uap.ctrl.tpl.qry.CpQueryTemplatePageModel;
import nc.uap.ctrl.tpl.qry.FromWhereSQLImpl;
import nc.uap.lfw.core.AppInteractionUtil;
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
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.serializer.impl.Datasets2AggVOSerializer;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.core.uif.delegator.IDataValidator;
import nc.ui.erm.billpub.action.HRATCommonUtils;
import nc.ui.erm.billpub.action.ServiceFeeTaxUtils;
import nc.ui.pub.print.IDataSource;
import nc.vo.arap.bx.util.BXParamConstant;
import nc.vo.arap.bx.util.BXUtil;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.BusiTypeVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.erm.accruedexpense.AccruedVerifyVO;
import nc.vo.erm.matterapp.MatterAppVO;
import nc.vo.erm.util.VOUtils;
import nc.vo.fipub.exception.ExceptionHandler;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import uap.web.bd.pub.AppUtil;

public class ExpMenuViewCtrl implements IController {
	
	public void add(MouseEvent mouseEvent) {
		AppUtil.addAppAttr("ncAssginUsers", null);

		String formID = "bxzb_base_info_form";
		if ("jkzb".equals(getMasterDsId())) {
			formID = "jkzb_base_info_form";
		}
		LfwView widget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) widget.getViewComponents().getComponent(
				formID);
		if (6 == form.getRenderType()) {
			String pageFlag = (String) AppUtil.getAppAttr("pageFlag");
			String url = (String) AppUtil.getAppAttr("DJURL") + "&sourcePage="
					+ pageFlag;
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.sendRedirect(
							LfwRuntimeEnvironment.getRootPath() + "/" + url);
		} else {
			UifCommand addCmd = new ExpUifAddCmd(getMasterDsId());
			addCmd.execute();
		}

		YerPageUtil.setBillTitleIsExpEdited(false);
	}

	public void addFromFysq(MouseEvent mouseEvent) {
		AppUtil.addAppAttr("ncAssginUsers", null);

		String formID = "bxzb_base_info_form";
		if ("jkzb".equals(getMasterDsId())) {
			formID = "jkzb_base_info_form";
		}
		LfwView widget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) widget.getViewComponents().getComponent(
				formID);

		if (6 == form.getRenderType()) {
			String pageFlag = (String) AppUtil.getAppAttr("pageFlag");
			String url = (String) AppUtil.getAppAttr("DJURL");
			url = url + "&isReadonlyFromFysq=Y&sourcePage=" + pageFlag;
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.sendRedirect(
							LfwRuntimeEnvironment.getRootPath() + "/" + url);
		} else {
			Map queryMap = new HashMap();

			String queryNode = "E1100301";
			String nodekey = (String) AppLifeCycleContext.current()
					.getApplicationContext().getAppAttribute("$QueryTemplate");

			queryMap.put("$QueryTemplate", queryNode);
			queryMap.put("nodekey", nodekey);
			queryMap.put("model", ExpFromFysqQueryPageModel.class.getName());

			queryMap.put("clc", ExpAdvancedQueryController.class.getName());

			OpenProperties props = new OpenProperties();
			props.setWidth("1000");
			props.setHeight("700");
			props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"er_nodes", "0E110YER-WEBERM-CODE-00002"));

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

		YerPageUtil.setBillTitleIsExpEdited(false);
	}

	public void commit(MouseEvent mouseEvent) {
		UifCommand commitCmd = new ExpUifCommitCmd(getMasterDsId(),
				getDetailDsIds(), BXVO.class.getName(), getBodyNotNull());

		commitCmd.execute();

		YerPageUtil.setBillTitleIsExpEdited(AppUtil
				.getAppAttr("EXP_ISEXPEDITED") == null ? false
				: ((Boolean) AppUtil.getAppAttr("EXP_ISEXPEDITED"))
						.booleanValue());
	}

	public void recall(MouseEvent mouseEvent) {
		AppUtil.addAppAttr("ncAssginUsers", null);
		UifCommand recallCmd = new ExpUifRecallCmd(getMasterDsId(),BXVO.class.getName());
		recallCmd.execute();
	}
	
	public void importfile(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset mailds = mainWidget.getViewModels().getDataset("bxzb");
		String djlxbm = mailds.getSelectedRow().getString(
				mailds.nameToIndex("djlxbm"));
		if (ServiceFeeTaxUtils.TRANSTYPE.equals(djlxbm)) {
			UifExcelImportCmd cmd = new UifExcelImportCmd(getClass().getName(),
					"viewid");
			CmdInvoker.invoke(cmd);
		}
	}
	
	
	/**
	 * 具体导入excel文件的方法
	 * 
	 * @param e
	 */
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
				throw new IllegalArgumentException("Excel文件导入失败!");
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
		// 得到表头数据集
		Dataset mailds = mainWidget.getViewModels().getDataset("bxzb");
		// 得到表体数据集
		Dataset ds = mainWidget.getViewModels().getDataset("busitem");
		String pk_org = mailds.getSelectedRow().getString(mailds.nameToIndex("pk_org"));
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
				emptyRow.setValue(ds.nameToIndex("pk_pcorg"), pk_org);
				
				// 劳务费类型对照
				if (HRATCommonUtils.isNotEmpty(l[0])) {
					// 劳务费类型对照
					String defitem50 = HRATCommonUtils.queryDefdocPk(l[0].toString(), "劳务费类型");
					emptyRow.setValue(ds.nameToIndex("defitem50"), defitem50);
				}
				
				// 收支项目对照转换
				if (HRATCommonUtils.isNotEmpty(l[1])) {
					// 收支项目对照转换
					String szxmid = HRATCommonUtils.queryInoutbusiclassByName(l[1].toString());
					emptyRow.setValue(ds.nameToIndex("szxmid"), szxmid);
				}
				
				// 工作内容
				if (HRATCommonUtils.isNotEmpty(l[2])) {
					emptyRow.setValue(ds.nameToIndex("defitem31"), l[2]);
				}
				
				// 劳务类别对照转换
				if (HRATCommonUtils.isNotEmpty(l[3])) {
					// 劳务类别对照转换	defitem49
					String defitem49 = HRATCommonUtils.queryDefdocPk(l[3].toString(), "劳务类别");
					emptyRow.setValue(ds.nameToIndex("defitem49"), defitem49);
				}
				
				// 姓名
				if (HRATCommonUtils.isNotEmpty(l[4])) {
					emptyRow.setValue(ds.nameToIndex("defitem48"), l[4]);
				}
				
				// 应发金额
				if (HRATCommonUtils.isNotEmpty(l[5])) {
					Double amount = Double.parseDouble(l[5].toString());
					emptyRow.setValue(ds.nameToIndex("amount"), amount);
				}
				
				// 实发金额
				if (HRATCommonUtils.isNotEmpty(l[6])) {
					emptyRow.setValue(ds.nameToIndex("defitem44"), l[6]);
				}
				
				// 证件类型对照转换
				if (HRATCommonUtils.isNotEmpty(l[7])) {
					// 证件类型对照转换
					String defitem43 = HRATCommonUtils.queryDefdocPk(l[7].toString(), "证件类型");
					emptyRow.setValue(ds.nameToIndex("defitem43"), defitem43);
				}
				
				// 证件号码
				if (HRATCommonUtils.isNotEmpty(l[8])) {
					emptyRow.setValue(ds.nameToIndex("defitem42"), l[8]);
				}
				
				// 国籍（区域）/ 出生国家（地区） 对照转换 defitem38  defitem41
				if (HRATCommonUtils.isNotEmpty(l[9]) && HRATCommonUtils.isNotEmpty(l[12])) {
					String national = HRATCommonUtils.queryDefdocPk(l[9].toString(), "国籍");
					emptyRow.setValue(ds.nameToIndex("defitem38"), national);
					emptyRow.setValue(ds.nameToIndex("defitem41"), national);
				}
				
				// 性别
				if (HRATCommonUtils.isNotEmpty(l[10])) {
					String sex = HRATCommonUtils.queryDefdocPk(l[10].toString(), "性别");
					emptyRow.setValue(ds.nameToIndex("defitem40"), sex);
				}
				
				// 出生日期
				if (HRATCommonUtils.isNotEmpty(l[11])) {
					emptyRow.setValue(ds.nameToIndex("defitem39"), l[11]);
				}
				
				// 手机号码
				if (HRATCommonUtils.isNotEmpty(l[13])) {
					emptyRow.setValue(ds.nameToIndex("defitem37"), l[13]);
				}
				
				// 是否已现场发放 对照转换
				if (HRATCommonUtils.isNotEmpty(l[14])) {
					String defitem36 = HRATCommonUtils.queryDefdocPk(l[14].toString(), "是否");
					emptyRow.setValue(ds.nameToIndex("defitem36"), defitem36);
				}
				
				// 银行类别 对照转换  defitem35
				if (HRATCommonUtils.isNotEmpty(l[15])) {
					String defitem35 = HRATCommonUtils.queryBankTypeByName(l[15].toString());
					emptyRow.setValue(ds.nameToIndex("defitem35"), defitem35);
				}
				
				// 开户银行 对照转换
				if (HRATCommonUtils.isNotEmpty(l[16])) {
					String defitem34 = HRATCommonUtils.queryBankByName(l[16].toString());
					emptyRow.setValue(ds.nameToIndex("defitem34"), defitem34);
				}
				
				// 银行卡号
				if (HRATCommonUtils.isNotEmpty(l[17])) {
					emptyRow.setValue(ds.nameToIndex("defitem33"), l[17]);
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		ds.setEnabled(true);
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

	/**
	 * 暂存方法 - 即点击保存时运行的方法
	 * Edit By 姚晶  尹成文 20191121
	 * 
	 * 湖北广电portal端 劳务费节点 导入Excel保存到界面上：
	 *  1.需要校验导入的数据的正确性
	 *  2.需要计算对应的金额
	 * 
	 * @param mouseEvent
	 */
	public void tempSave(MouseEvent mouseEvent) {
		
		UifCommand tempSaveCmd = new ExpUifTempSaveCmd(getMasterDsId(),
				getDetailDsIds(), BXVO.class.getName(), getBodyNotNull());
		
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		
		// 得到表头数据集
		Dataset headData = mainWidget.getViewModels().getDataset("bxzb");
		// 得到表体数据集
		Dataset BodyData = mainWidget.getViewModels().getDataset("busitem");
		
		/**
		 * 想在暂存方法的同时先校验一下页面中的必填项 
		 * 后期待完善。。。    Start
		 */
//		List<Dataset> detailDs = new ArrayList<Dataset>();
//		detailDs.add(BodyData);
//		// 校验页面上的必输项
//		doValidate(headData, detailDs);
		/**
		 * End
		 */
		
		// 封装一个单独的类用来处理计算与校验的逻辑
		SaveToCaculateAndCheck calAndCheck = new SaveToCaculateAndCheck(mainWidget, headData, BodyData);
		// 先校验
		String errorMsg = calAndCheck.checkData();
		
		if(HRATCommonUtils.isEmpty(errorMsg)){
			// 校验通过再计算,并给界面上传值
			calAndCheck.caculate();
		}else{
			AppInteractionUtil.showErrorDialog(errorMsg, "警告", "确认");
		}
		
		
		
		
		
		tempSaveCmd.execute();
		YerPageUtil.setBillTitleIsExpEdited(AppUtil
				.getAppAttr("EXP_ISEXPEDITED") == null ? false
				: ((Boolean) AppUtil.getAppAttr("EXP_ISEXPEDITED")).booleanValue());
		
	}

	public void save(MouseEvent mouseEvent) {
		AppUtil.addAppAttr("is_jkbx_menu_open", "Y");
		UifCommand saveCmd = new ExpUifSaveCmd(getMasterDsId(),
				getDetailDsIds(), BXVO.class.getName(), getBodyNotNull());

		saveCmd.execute();

		YerPageUtil.setBillTitleIsExpEdited(AppUtil
				.getAppAttr("EXP_ISEXPEDITED") == null ? false
				: ((Boolean) AppUtil.getAppAttr("EXP_ISEXPEDITED"))
						.booleanValue());
	}

	public void copy(MouseEvent mouseEvent) {
		AppUtil.addAppAttr("ncAssginUsers", null);
		String formID = "bxzb_base_info_form";
		if ("jkzb".equals(getMasterDsId())) {
			formID = "jkzb_base_info_form";
		}
		LfwView widget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) widget.getViewComponents().getComponent(
				formID);
		if (6 == form.getRenderType()) {
			String pageFlag = (String) AppUtil.getAppAttr("pageFlag");
			Dataset masterDs = widget.getViewModels().getDataset(
					getMasterDsId());
			Row row = masterDs.getSelectedRow();
			if (row == null) {
				return;
			}
			
			String pk_bill = (String) row.getValue(masterDs
					.nameToIndex("pk_jkbx"));
			int djzt = ((Integer) row.getValue(masterDs.nameToIndex("djzt")))
					.intValue();
			String url = (String) AppUtil.getAppAttr("DJURL");
			url = url + "&sourcePage=" + pageFlag + "&taskPk=ncerwfmtaskqry";
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
		String pageFlag = (String) AppUtil.getAppAttr("pageFlag");
		int djzt = jkbxVO.getParentVO().getDjzt().intValue();
		if ((djzt != 0) && (djzt != 1)) {
			String pk_bill = jkbxVO.getParentVO().getPk_jkbx();
			String url = (String) AppUtil.getAppAttr("DJURL");
			url = url + "&sourcePage=" + pageFlag + "&taskPk=ncerwfmtaskqry";
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
		Row headRow = bxheadDs.getSelectedRow();
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
				Row[] row = busitemDs.getCurrentRowData().getRows();
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

		String exp_is_mactrl = (String) AppUtil.getAppAttr("exp_is_mactrl");
		if ((exp_is_mactrl != null) && ("Y".equals(exp_is_mactrl))) {
			String pk_item = (String) headRow.getValue(bxheadDs
					.nameToIndex("pk_item"));
			if ((pk_item == null) || ("".equals(pk_item))) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("per_codes", "0per_codes0422"));
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

	public void hxyt(MouseEvent mouseEvent) {
		JKBXVO jkbxvo = getJKBXVO();
		if (jkbxvo == null) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("per_codes", "0per_codes0443"));
		}

		try {
			checkBxVOValid(jkbxvo);
		} catch (ValidationException e) {
			throw new LfwRuntimeException(e.getMessage());
		}
		String whereStr = " 1=1 ";
		FromWhereSQLImpl whereSql = new FromWhereSQLImpl();
		whereSql.setWhere(whereStr);
		LfwRuntimeEnvironment.getWebContext().getRequest().getSession()
				.setAttribute("exp_jkbxvo", jkbxvo);
		AppUtil.addAppAttr("hxyt_whereSql", whereSql);
		AppUtil.addAppAttr("hxyt_whereSql_flag", "N");
		AppLifeCycleContext
				.current()
				.getWindowContext()
				.popView(
						"hxyt",
						"850",
						"578",
						NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"per_codes", "0per_codes0439"), false);
	}

	private JKBXVO getJKBXVO() {
		ViewContext viewCtx = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main");

		LfwView widget = viewCtx.getView();
		if (widget == null) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("per_codes", "0per_codes0010"));
		}
		String masterDsId = getMasterDsId();
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("per_codes", "0per_codes0009")
					+ masterDsId
					+ "!");
		}
		String[] detailDsIds = getDetailDsIds();
		List idList = new ArrayList();
		idList.add(masterDsId);
		if ((detailDsIds != null) && (detailDsIds.length > 0)) {
			idList.addAll(Arrays.asList(detailDsIds));
		}
		ArrayList detailDs = new ArrayList();
		if ((detailDsIds != null) && (detailDsIds.length > 0)) {
			for (int i = 0; i < detailDsIds.length; i++) {
				Dataset ds = widget.getViewModels().getDataset(detailDsIds[i]);
				if (ds != null) {
					if (("finitem".equals(ds.getId()))
							|| ("jkfinitem".equals(ds.getId()))) {
						ds.getCurrentRowSet().clear();
					}
					detailDs.add(ds);
				}
			}
		}
		Datasets2AggVOSerializer serializer = new ExpDatasets2AggVOSerializer();
		Dataset[] detailDss = (Dataset[]) detailDs.toArray(new Dataset[0]);
		AggregatedValueObject aggVo = serializer.serialize(masterDs, detailDss,
				BXVO.class.getName());

		JKBXVO jkbxvo = (JKBXVO) aggVo;
		return jkbxvo;
	}

	private void checkBxVOValid(JKBXVO vo) throws ValidationException {
		StringBuffer bf = new StringBuffer();
		UFBoolean iscostshare = vo.getParentVO().getIscostshare();
		if ((iscostshare != null) && (iscostshare.booleanValue())) {
			bf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("per_codes",
					"0per_codes0444"));
		}
		UFBoolean isexp = vo.getParentVO().getIsexpamt();
		if ((isexp != null) && (isexp.booleanValue())) {
			bf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("per_codes",
					"0per_codes0445"));
		}

		String pk_item = vo.getParentVO().getPk_item();
		if ((pk_item != null) && (pk_item.length() > 0)) {
			bf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("per_codes",
					"0per_codes0463"));
		}

		if (bf.length() != 0)
			throw new ValidationException(bf.toString());
	}

	public void quickShare(MouseEvent mouseEvent) {
		LfwWindow bxWindow = AppLifeCycleContext.current().getWindowContext()
				.getWindow();
		Dataset bxheadDs = bxWindow.getView("main").getViewModels()
				.getDataset("bxzb");
		Row headRow = bxheadDs.getSelectedRow();

		UFDouble totalAmount = (UFDouble) headRow.getValue(bxheadDs
				.nameToIndex("total"));
		Dataset bx_accrued_verify = bxWindow.getView("main").getViewModels()
				.getDataset("bx_accrued_verify");
		if (bx_accrued_verify.getCurrentRowData() != null) {
			Row[] row = bx_accrued_verify.getCurrentRowData().getRows();
			if ((row != null) && (row.length > 0)) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("per_codes", "0per_codes0448"));
			}
		}
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

		String exp_is_mactrl = (String) AppUtil.getAppAttr("exp_is_mactrl");
		if ((exp_is_mactrl != null) && ("Y".equals(exp_is_mactrl))) {
			String pk_item = (String) headRow.getValue(bxheadDs
					.nameToIndex("pk_item"));
			if ((pk_item == null) || ("".equals(pk_item))) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("per_codes", "0per_codes0422"));
			}
		}

		AppLifeCycleContext
				.current()
				.getWindowContext()
				.popView(
						"quickshare",
						"600",
						"320",
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
						buf.append("showDialog(\"")
								.append(url)
								.append("\", \"")
								.append(NCLangRes4VoTransl.getNCLangRes()
										.getStrByID("uapquerytemplate",
												"SimpleQueryController-000000"))
								.append("\", ").append("800").append(",")
								.append("600").append(", \"")
								.append("queryTemplate")
								.append("\",   true  ,  true );").toString());
	}

	public void backToCenter(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = mainWidget.getViewModels().getDataset(getMasterDsId());
		Row row = ds.getSelectedRow();
		String billType;
		if (row == null)
			billType = (String) AppUtil.getAppAttr("default_djlxbm");
		else {
			billType = (String) row.getValue(ds.nameToIndex("djlxbm"));
		}
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
		Properties tProperties = YerUtil
				.loadNodePropertie("yercommon.properties");
		String isNCPrint = "";
		if (tProperties == null) {
			isNCPrint = "N";
		}
		isNCPrint = tProperties.getProperty("isncprint");
		if ((isNCPrint != null) && ("Y".equals(isNCPrint))) {
			IDataSource printDs = null;
			String funCode = "20110BMLB";
			String pkJkbx = AppLifeCycleContext.current().getParameter(
					"pk_jkbx");
			String djdl = AppLifeCycleContext.current().getParameter("djdl");
			if ((pkJkbx == null) || ("".endsWith(pkJkbx)) || (djdl == null)
					|| ("".equals(djdl))) {
				LfwView mainWidget = AppLifeCycleContext.current()
						.getWindowContext().getViewContext("main").getView();
				Dataset masterDs = mainWidget.getViewModels().getDataset(
						getMasterDsId());
				if (masterDs.getSelectedRow() == null) {
					throw new LfwRuntimeException("请先选择要打印的单据!");
				}
				pkJkbx = (String) masterDs.getSelectedRow().getValue(
						masterDs.nameToIndex("pk_jkbx"));
				djdl = (String) masterDs.getSelectedRow().getValue(
						masterDs.nameToIndex("djdl"));
			}
			try {
				List voList = ((IBXBillPrivate) NCLocator.getInstance().lookup(
						IBXBillPrivate.class.getName())).queryVOsByPrimaryKeys(
						new String[] { pkJkbx }, djdl);
				JKBXVO bxvo = (JKBXVO) voList.get(0);
				String billid = bxvo.getParentVO().getPrimaryKey();
				JKBXHeaderVO head = bxvo.getParentVO();
				String nodeKey = head.getDjlxbm();
				printDs = new ExpPrintDataSource(billid, head.getDjlxbm(),
						head.getAuditman(), voList);
				UifCommand printCommand = new YerCommonPrintCmd(
						getMasterDsId(), funCode, null, nodeKey, printDs);
				printCommand.execute();
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new LfwRuntimeException(e.getMessage(), e);
			}
		} else {
			String pk_jkbx = "";
			String djdl = "";
			String winId = "";

			pk_jkbx = AppLifeCycleContext.current().getParameter("pk_jkbx");
			djdl = AppLifeCycleContext.current().getParameter("djdl");
			String nodecode = AppLifeCycleContext.current().getParameter(
					"nodecode");
			if ((pk_jkbx == null) || ("".endsWith(pk_jkbx)) || (djdl == null)
					|| ("".equals(djdl))) {
				ViewContext viewContex = AppLifeCycleContext.current()
						.getWindowContext().getViewContext("main");
				AppLifeCycleContext.current().getWindowContext()
						.setCurrentViewContext(viewContex);
				LfwView print = viewContex.getView();
				Dataset ds = print.getViewModels().getDataset(getMasterDsId());
				Row row = ds.getSelectedRow();
				pk_jkbx = (String) row.getValue(ds.nameToIndex("pk_jkbx"));
				djdl = (String) row.getValue(ds.nameToIndex("djdl"));
				if ((pk_jkbx == null) || ("".endsWith(pk_jkbx))
						|| (djdl == null) || ("".equals(djdl))) {
					throw new LfwRuntimeException(NCLangRes4VoTransl
							.getNCLangRes().getStrByID("yer",
									"0E110YER-V63-005"));
				}
				if (nodecode == null) {
					nodecode = (String) LfwRuntimeEnvironment.getWebContext()
							.getAppSession().getAttribute("nodecode");
				}
				winId = AppLifeCycleContext.current().getWindowContext()
						.getId();
			} else {
				winId = YerUtil.getColValue("cp_appsnode", "appid", "id",
						nodecode);
				if ((winId != null) && (!"".equals(winId))) {
					winId = winId.replace("app_", "");
				}
			}
			if (("".equals(winId))
					&& ((nodecode == null) || ("".equals(nodecode)))) {
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
				else {
					printTemplate.print(expPrintServiceImpl);
				}
			} catch (IllegalArgumentException e1) {
				Logger.error(e1.getMessage(), e1);
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("webea_nodes", "w_fysq-000150"));
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new LfwRuntimeException(e.getMessage(), e);
			}
		}
	}

	public void printCenter(ScriptEvent scriptEvent) {
		print(null);
	}

	public void onFileManagerClick(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = mainWidget.getViewModels().getDataset(getMasterDsId());
		Row row = ds.getSelectedRow();
		String pk_jkbx = (String) row.getValue(ds.nameToIndex("pk_jkbx"));
		String oID = "";
		if ((pk_jkbx == null) || ("".equals(pk_jkbx))) {
			IdGenerator idgenerator = (IdGenerator) NCLocator.getInstance()
					.lookup(IdGenerator.class);
			oID = idgenerator.generate();
			AppUtil.addAppAttr("pk_jkbx", oID);
			String existFlag = "N";
			AppUtil.addAppAttr("isExisted", existFlag);
			String enabledFlag = "Y";
			AppUtil.addAppAttr("isMenuEnabled", enabledFlag);
			ApplicationContext appCtx = AppLifeCycleContext.current()
					.getApplicationContext();
			Map paraMap = new HashMap();
			String nodecode = (String) LfwRuntimeEnvironment.getWebContext()
					.getAppSession().getAttribute("nodecode");
			paraMap.put("nodecode", nodecode);
			appCtx.navgateTo("filemanager", NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("per_codes", "0per_codes0015"), "750", "450",
					paraMap);
		} else {
			AppUtil.addAppAttr("pk_jkbx", pk_jkbx);

			String spzt = String.valueOf(row.getValue(ds.nameToIndex("spzt")));

			String existFlag = "Y";
			if (!ExpUtil.isBxJkExit(pk_jkbx)) {
				existFlag = "N";
			}

			AppUtil.addAppAttr("isExisted", existFlag);

			String enabledFlag = "Y";

			if (-1 != Integer.valueOf(spzt).intValue()) {
				enabledFlag = "N";
			}

			AppUtil.addAppAttr("isMenuEnabled", enabledFlag);

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
	}

	public void linkSQDinfoFromJKD(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		String dsID = getMasterDsId();
		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
		Row currentRow = masterDs.getSelectedRow();
		if (currentRow != null)
			try {
				String pk_jkbx = (String) currentRow.getValue(masterDs
						.nameToIndex("pk_jkbx"));
				if ((pk_jkbx == null) || ("".equals(pk_jkbx))) {
					AppInteractionUtil.showMessageDialog("确保当前单据已保存。");
					return;
				}

				List headVOlist = ((IBXBillPrivate) NCLocator.getInstance()
						.lookup(IBXBillPrivate.class.getName()))
						.queryHeadersByPrimaryKeys(new String[] { pk_jkbx },
								"jk");

				if ((headVOlist != null) && (headVOlist.size() > 0)) {
					JKBXHeaderVO headVO = (JKBXHeaderVO) headVOlist.get(0);
					String fysqID = headVO.getPk_item();
					if ((fysqID == null) || ("".equals(fysqID))) {
						AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
								.getNCLangRes().getStrByID("weberm_0",
										"0E010001-00121"));
						return;
					}

					MatterAppVO[] matterAppVOArr = ((IErmMatterAppBillQuery) NCLocator
							.getInstance().lookup(
									IErmMatterAppBillQuery.class.getName()))
							.queryMatterAppVoByPks(new String[] { fysqID });
					String tradeType = matterAppVOArr[0].getPk_tradetype();

					String urlPart = (String) ExpUtil.getBilltypeUrlMap().get(
							tradeType);

					String url = "/portal/" + urlPart + "&openBillId=" + fysqID
							+ "&spzt=3&islinkfysq=Y";

					ApplicationContext ctx = AppLifeCycleContext.current()
							.getApplicationContext();

					ctx.popOuterWindow(url, "费用申请单", "1200", "90%",
							"TYPE_DIALOG");
				}

			} catch (BusinessException e) {
				e.printStackTrace();
			}
	}

	public void linkSQDinfo(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		String dsID = getMasterDsId();
		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
		Row currentRow = masterDs.getSelectedRow();
		if (currentRow != null)
			try {
				String pk_jkbx = (String) currentRow.getValue(masterDs
						.nameToIndex("pk_jkbx"));
				if ((pk_jkbx == null) || ("".equals(pk_jkbx))) {
					AppInteractionUtil.showMessageDialog("确保当前单据已保存。");
					return;
				}

				List headVOlist = ((IBXBillPrivate) NCLocator.getInstance()
						.lookup(IBXBillPrivate.class.getName()))
						.queryHeadersByPrimaryKeys(new String[] { pk_jkbx },
								"bx");

				if ((headVOlist != null) && (headVOlist.size() > 0)) {
					JKBXHeaderVO headVO = (JKBXHeaderVO) headVOlist.get(0);
					String fysqID = headVO.getPk_item();
					if ((fysqID == null) || ("".equals(fysqID))) {
						AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
								.getNCLangRes().getStrByID("weberm_0",
										"0E010001-00121"));
						return;
					}

					MatterAppVO[] matterAppVOArr = ((IErmMatterAppBillQuery) NCLocator
							.getInstance().lookup(
									IErmMatterAppBillQuery.class.getName()))
							.queryMatterAppVoByPks(new String[] { fysqID });
					String tradeType = matterAppVOArr[0].getPk_tradetype();

					String urlPart = (String) ExpUtil.getBilltypeUrlMap().get(
							tradeType);

					String url = "/portal/" + urlPart + "&openBillId=" + fysqID
							+ "&spzt=3&islinkfysq=Y";

					ApplicationContext ctx = AppLifeCycleContext.current()
							.getApplicationContext();

					ctx.popOuterWindow(url, "费用申请单", "1200", "90%",
							"TYPE_DIALOG");
				}

			} catch (BusinessException e) {
				e.printStackTrace();
			}
	}

	public void linkBXDinfo(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		String dsID = getMasterDsId();
		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
		Row currentRow = masterDs.getSelectedRow();
		if (currentRow != null) {
			String[] detailDsIds = getDetailDsIds();
			ArrayList detailDs = new ArrayList();
			if ((detailDsIds != null) && (detailDsIds.length > 0)) {
				for (int i = 0; i < detailDsIds.length; i++) {
					Dataset ds = mainWidget.getViewModels().getDataset(
							detailDsIds[i]);
					if (ds != null) {
						if (("finitem".equals(ds.getId()))
								|| ("jkfinitem".equals(ds.getId()))) {
							ds.getCurrentRowSet().clear();
						}
						detailDs.add(ds);
					}
				}
			}
			Datasets2AggVOSerializer serializer = new ExpDatasets2AggVOSerializer();
			Dataset[] detailDss = (Dataset[]) detailDs.toArray(new Dataset[0]);
			AggregatedValueObject aggVo = serializer.serialize(masterDs,
					detailDss, BXVO.class.getName());
			JKBXVO jkbxvo = (JKBXVO) aggVo;
			JKBXHeaderVO parentVO = jkbxvo.getParentVO();
			Collection contrasts = null;
			try {
				contrasts = ((IBXBillPrivate) NCLocator.getInstance().lookup(
						IBXBillPrivate.class)).queryContrasts(parentVO);
				if ((contrasts == null) || (contrasts.size() == 0)) {
					AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
							.getNCLangRes().getStrByID("weberm_0",
									"0E010001-00123"));
					return;
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}

			String[] oids = VOUtils
					.changeCollectionToArray(contrasts, "pk_bxd");
			List pkset = new ArrayList();
			String pksetStr = "";
			for (int i = 0; i < oids.length; i++) {
				if (!pkset.contains(oids[i])) {
					pkset.add(oids[i]);
					pksetStr = pksetStr + oids[i] + ",";
				}
			}
			if ((pksetStr != null) && (pksetStr.length() > 0)) {
				pksetStr = pksetStr.substring(0, pksetStr.length() - 1);
			}

			Map paramMap = new HashMap();
			paramMap.put("model", LinkBXDPageModel.class.getName());

			paramMap.put("pksetStr", pksetStr);
			if ((pksetStr == null) || ("".equals(pksetStr))) {
				AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("weberm_0", "0E010001-00123"));
				return;
			}

			OpenProperties props = new OpenProperties();
			props.setWidth("1000");
			props.setHeight("600");
			props.setTitle("联查报销单");
			props.setOpenId("linkbxd");
			props.setParamMap(paramMap);
			AppLifeCycleContext.current().getViewContext().navgateTo(props);
		}
	}

	public void linkYTDinfo(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		String dsID = getMasterDsId();
		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
		Row currentRow = masterDs.getSelectedRow();
		if (currentRow != null) {
			String[] detailDsIds = getDetailDsIds();
			ArrayList detailDs = new ArrayList();
			if ((detailDsIds != null) && (detailDsIds.length > 0)) {
				for (int i = 0; i < detailDsIds.length; i++) {
					Dataset ds = mainWidget.getViewModels().getDataset(
							detailDsIds[i]);
					if (ds != null) {
						if (("finitem".equals(ds.getId()))
								|| ("jkfinitem".equals(ds.getId()))) {
							ds.getCurrentRowSet().clear();
						}
						detailDs.add(ds);
					}
				}
			}
			Datasets2AggVOSerializer serializer = new ExpDatasets2AggVOSerializer();
			Dataset[] detailDss = (Dataset[]) detailDs.toArray(new Dataset[0]);
			AggregatedValueObject aggVo = serializer.serialize(masterDs,
					detailDss, BXVO.class.getName());
			JKBXVO jkbxvo = (JKBXVO) aggVo;
			AccruedVerifyVO[] verifyVOArr = jkbxvo.getAccruedVerifyVO();
			if ((verifyVOArr == null) || (verifyVOArr.length < 1)) {
				AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("weberm_0", "0E010001-00122"));
				return;
			}
			String pksetStr = "";
			List accpks = new ArrayList();
			for (AccruedVerifyVO verifyVO : verifyVOArr) {
				if (!accpks.contains(verifyVO.getPk_accrued_bill())) {
					accpks.add(verifyVO.getPk_accrued_bill());
					pksetStr = pksetStr + verifyVO.getPk_accrued_bill() + ",";
				}
			}

			if ((pksetStr == null) || ("".equals(pksetStr))) {
				AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("weberm_0", "0E010001-00122"));
				return;
			}
			if ((pksetStr != null) && (pksetStr.length() > 0)) {
				pksetStr = pksetStr.substring(0, pksetStr.length() - 1);
			}

			Map paramMap = new HashMap();
			paramMap.put("model", LinkYTDPageModel.class.getName());
			paramMap.put("pksetStr", pksetStr);

			OpenProperties props = new OpenProperties();
			props.setWidth("1000");
			props.setHeight("600");
			props.setTitle("联查预提单");
			props.setOpenId("linkytd");
			props.setParamMap(paramMap);
			AppLifeCycleContext.current().getViewContext().navgateTo(props);
		}
	}

	public void linkJKDinfo(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		String dsID = getMasterDsId();
		Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
		Row currentRow = masterDs.getSelectedRow();

		if (currentRow != null) {
			String[] detailDsIds = getDetailDsIds();
			ArrayList detailDs = new ArrayList();
			if ((detailDsIds != null) && (detailDsIds.length > 0)) {
				for (int i = 0; i < detailDsIds.length; i++) {
					Dataset ds = mainWidget.getViewModels().getDataset(
							detailDsIds[i]);
					if (ds != null) {
						if (("finitem".equals(ds.getId()))
								|| ("jkfinitem".equals(ds.getId()))) {
							ds.getCurrentRowSet().clear();
						}
						detailDs.add(ds);
					}
				}
			}
			Datasets2AggVOSerializer serializer = new ExpDatasets2AggVOSerializer();
			Dataset[] detailDss = (Dataset[]) detailDs.toArray(new Dataset[0]);
			AggregatedValueObject aggVo = serializer.serialize(masterDs,
					detailDss, BXVO.class.getName());
			JKBXVO jkbxvo = (JKBXVO) aggVo;
			JKBXHeaderVO parentVO = jkbxvo.getParentVO();
			Collection contrasts = null;
			try {
				contrasts = ((IBXBillPrivate) NCLocator.getInstance().lookup(
						IBXBillPrivate.class)).queryContrasts(parentVO);
				if ((contrasts == null) || (contrasts.size() == 0)) {
					throw new BusinessException(NCLangRes4VoTransl
							.getNCLangRes().getStrByID("2011v61013_0",
									"02011v61013-0100"));
				}

			} catch (BusinessException e) {
				e.printStackTrace();
			}

			String[] oids = VOUtils
					.changeCollectionToArray(contrasts, "pk_jkd");
			List pkset = new ArrayList();
			String pksetStr = "";
			for (int i = 0; i < oids.length; i++) {
				if (!pkset.contains(oids[i])) {
					pkset.add(oids[i]);
					pksetStr = pksetStr + oids[i] + ",";
				}
			}
			if ((pksetStr != null) && (pksetStr.length() > 0)) {
				pksetStr = pksetStr.substring(0, pksetStr.length() - 1);
			}

			Map paramMap = new HashMap();
			paramMap.put("model", LinkJKDPageModel.class.getName());

			paramMap.put("pksetStr", pksetStr);
			if ((pksetStr == null) || ("".equals(pksetStr))) {
				AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
						.getNCLangRes().getStrByID("2011v61013_0",
								"02011v61013-0100"));

				return;
			}

			OpenProperties props = new OpenProperties();
			props.setWidth("1000");
			props.setHeight("600");
			props.setTitle("联查借款单");
			props.setOpenId("linkjkd");
			props.setParamMap(paramMap);
			AppLifeCycleContext.current().getViewContext().navgateTo(props);
		}
	}

	public void linkPfinfoCenter(ScriptEvent scriptEvent) {
		linkPfinfo(null);
	}

	public void linkPfinfo(MouseEvent mouseEvent) {
		String billid = AppLifeCycleContext.current().getParameter("pk_jkbx");
		String pk_org = AppLifeCycleContext.current().getParameter("pk_org");
		String billtype = AppLifeCycleContext.current()
				.getParameter("billtype");

		if ((billid == null) || ("".equals(billid))) {
			LfwView mainWidget = AppLifeCycleContext.current()
					.getWindowContext().getViewContext("main").getView();
			String dsID = getMasterDsId();
			Dataset masterDs = mainWidget.getViewModels().getDataset(dsID);
			Row currentRow = masterDs.getSelectedRow();
			if (currentRow != null) {
				billtype = (String) currentRow.getValue(masterDs
						.nameToIndex("djlxbm"));
				billid = (String) currentRow.getValue(masterDs
						.nameToIndex("pk_jkbx"));
				pk_org = (String) currentRow.getValue(masterDs
						.nameToIndex("pk_org"));
			}

		}

		try {
			String paraString = SysInitQuery.getParaString(pk_org,
					BXParamConstant.ER_FLOW_TYPE);
			if ("2".equals(paraString)) {
				Map paramMap = new HashMap();

				paramMap.put("model",
						YerWorkflowPfinfoPageModel.class.getName());
				paramMap.put("billType", billtype);
				paramMap.put("billId", billid);

				OpenProperties props = new OpenProperties();
				props.setWidth("1000");
				props.setHeight("600");
				props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"per_codes", "0per_codes0052"));
				props.setOpenId("workflowpfinfo");
				props.setParamMap(paramMap);
				AppLifeCycleContext.current().getViewContext().navgateTo(props);
			} else {
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
		} catch (BusinessException e) {
			ExceptionHandler.consume(e);
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
		Row currentRow = masterDs.getSelectedRow();
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
		Row currentRow = masterDs.getSelectedRow();
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

	public void linkVoucher(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = mainWidget.getViewModels().getDataset(getMasterDsId());
		Row row = ds.getSelectedRow();
		String pk_bill = (String) row.getValue(ds.nameToIndex("pk_jkbx"));
		String djdl = (String) row.getValue(ds.nameToIndex("djdl"));

		String url = LfwRuntimeEnvironment.getCorePath()
				+ "/pt/yercommon/linkVoucher?pkBill=" + pk_bill + "&djdl="
				+ djdl + "&billtypecode=264";

		ApplicationContext ctx = AppLifeCycleContext.current()
				.getApplicationContext();

		ctx.popOuterWindow(url, "联查凭证", "890", "500", "TYPE_DIALOG");
	}

	public void invalid(MouseEvent mouseEvent) {
		UifCommand invalidCmd = new ExpUifInvalidCmd(getMasterDsId(),
				getDetailDsIds(), BXVO.class.getName(), getBodyNotNull());

		invalidCmd.execute();
	}

	public void imageShow(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = mainWidget.getViewModels().getDataset(getMasterDsId());
		Row row = ds.getSelectedRow();
		String pk_jkbx = (String) row.getValue(ds.nameToIndex("pk_jkbx"));
		String pk_group = (String) row.getValue(ds.nameToIndex("pk_group"));
		YerPageUtil.imageShow(pk_jkbx, pk_group);
	}

	public void imageUpload(MouseEvent mouseEvent) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = mainWidget.getViewModels().getDataset(getMasterDsId());
		Row row = ds.getSelectedRow();
		String pk_jkbx = (String) row.getValue(ds.nameToIndex("pk_jkbx"));

		String pk_org = (String) row.getValue(ds.nameToIndex("pk_org"));
		String djlxbm = (String) row.getValue(ds.nameToIndex("djlxbm"));
		String operator = (String) row.getValue(ds.nameToIndex("jkbxr"));
		String creator = (String) row.getValue(ds.nameToIndex("creator"));
		String billno = (String) row.getValue(ds.nameToIndex("djbh"));
		int spzt = row.getValue(ds.nameToIndex("spzt")) == null ? -9999
				: ((Integer) row.getValue(ds.nameToIndex("spzt"))).intValue();
		String pk_group = (String) row.getValue(ds.nameToIndex("pk_group"));
		YerPageUtil.imageUpload(pk_jkbx, djlxbm, spzt, operator, creator,
				pk_org, pk_group, billno);
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
	
	
	protected void doValidate(Dataset masterDs, List<Dataset> detailDs)
			throws LfwValidateException {
		IDataValidator validator = getValidator();
		validator.validate(masterDs, new LfwView());
		if (detailDs != null) {
			int size = detailDs.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Dataset ds = (Dataset) detailDs.get(i);
					validator.validate(ds, new LfwView());
				}
			}
		}
	}
	
	protected IDataValidator getValidator() {
	    return new DefaultDataValidator();
	}
	
}