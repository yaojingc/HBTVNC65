package nc.ui.erm.billpub.view.eventhandler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.erm.billpub.action.HRATCommonUtils;
import nc.ui.erm.billpub.action.IdCardUtil;
import nc.ui.erm.billpub.action.ServiceFeeTaxUtils;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import java.util.Objects;

public class BodyAfterEditHandler {

	private ErmBillBillForm editor = null;

	// 定义一个常量存放劳务费报销单的交易类型
	private static final String TRANSTYPE = "264X-Cxx-01";

	public BodyAfterEditHandler(ErmBillBillForm editor) {
		this.editor = editor;
	}

	/**
	 * 
	 * @Title: doEdit
	 * @Description: 具体处理编辑事件赋值的方法
	 * @param @param editField 传进来的正在编辑的字段名
	 * @return void 返回类型
	 * @throws
	 */
	public void doEdit(String editField, int row) {
		// 获取交易类型
		String selectBillTypeCode = ((ErmBillBillManageModel) editor.getModel()).getSelectBillTypeCode();
		// 判断交易类型是否是劳务费
		if (TRANSTYPE.equals(selectBillTypeCode)) {
			switch (editField) {
			case "defitem50":
				// 校验1：验证劳务费类型，当切换劳务费类型时清空五个数值
				this.check_01(editField, row);

				break;

			case "defitem37":
				// 校验2： 验证手机号码
				this.check_02(editField, row);

				break;

			case "defitem42":
				// 校验2_1：身份信息
				this.check_02(editField, row);
				// 校验7：UAP人员档案状态为启用、组织为台本级，人员类别为xx时，根据档案中的身份证号码识别，不能填报劳务费报销单校验。
				this.check_07(editField, row);
				break;

			case "defitem36":
				// 校验3：验证是否已现场发放 字段
				this.check_03(editField, row);

				break;

			case "defitem48":
				// 校验4：验证姓名与身份证号与第一次填写报销单时要相匹配
				this.check_04(editField, row);

				break;
			case "defitem43":
				// 校验5：切换证件类型相应表格内容清空
				this.check_05(editField, row);

				break;

			case "szxmid":
				// 校验6：切换收支项目后 清空 五个数值
				this.check_06(editField, row);

				break;

			case "amount":
				// 计算1 : 根据应发金额计算 本次预计扣税 ，实发金额
				this.setValue_01(editField, row);

				break;

			case "defitem44":
				// 计算2 : 根据实发金额计算 本次预计扣税 ，应发金额
				this.setValue_02(editField, row);

				break;

			default:
				break;
			}

			/**
			 * something TODO
			 */
		}

	}

	/**
	 * UAP人员档案状态为启用、组织为台本级，根据档案中的身份证号码识别，不能填报劳务费报销单校验。人员类别为离退休、离职可以报销劳务费，
	 * 
	 */
	private void check_07(String editField, int row) {
		// 得到前台填写的身份证号码
		String idCardNo = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem42");
		// 得到前台填写的姓名
		String name = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem48");

		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		SqlBuilder sql = new SqlBuilder();
		// 当填写的身份证号和姓名都不为空的时候执行校验
		if (HRATCommonUtils.isNotEmpty(idCardNo) && HRATCommonUtils.isNotEmpty(name)) {
			sql.append("select name, id from BD_PSNDOC where PK_PSNDOC in(select PK_PSNDOC from HI_PSNJOB where PK_PSNCL in (select pk_psncl from BD_PSNCL where NAME in ('实名制', '代理制', '派遣制', '企业自聘', '转企改制', '其他', '内退')))");
			sql.append(" and name='");
			sql.append(name);
			sql.append("'");
			sql.append(" and id='");
			sql.append(idCardNo);
			sql.append("'");
			sql.append(" and enablestate=2");	//启用状态 是否启用
			sql.append(" and PK_ORG =(select PK_ORG from org_orgs where  name = '湖北广播电视台台本级')");//台本级校验
			try {
				Object result = queryBS.executeQuery(sql.toString(), new ColumnProcessor());
				// 如果数据库中能够查询到数据,说明改用户不符合,置空相应字段
				if (result != null) {
					MessageDialog.showErrorDlg(null, "警告:", "第" + (row + 1) + " 行的人员是在职人员,不允许报销劳务费，请核对后填写！");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem48");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem42");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem41");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem40");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem38");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem39");
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 
	 * 得到应发金额，计算出本次预计扣税 和 实发金额
	 * 
	 * 应发金额 ：amount 本次预计扣税 ： defitem46 实发金额 : defitem44 收支项目:szxmid
	 */
	private void setValue_01(String editField, int row) {
		// 获取收支项目的值
		String incomeType = editor.getBillCardPanel().getBodyValueAt(row, "szxmid").toString();
		// 得到 amount 应发金额
		Double amountBefore = Double.parseDouble(editor.getBillCardPanel().getBodyValueAt(row, "amount").toString());
		if (amountBefore < 0) {
			// 如果输入负值工资 ,给出提示信息,并且清除相应表格的数据
			MessageDialog.showErrorDlg(null, "警告", "请输入正确金额,应发金额不能为负值!");
			editor.getBillCardPanel().setBodyValueAt(null, row, "amount");
			editor.getBillCardPanel().setBodyValueAt(null, row, "defitem44");
			editor.getBillCardPanel().setBodyValueAt(null, row, "defitem46");
		} else {
			// 根据收支项目的类型来计算实发金额
			Double amountAfter = 0D;
			if (incomeType != null && incomeType.contains("劳务费")) {
				amountAfter = ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(amountBefore);
			} else if (incomeType != null && incomeType.contains("稿费")) {
				amountAfter = ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(amountBefore);
			}
			// 本次预计扣税
			Double tax = amountBefore - amountAfter;
			// 向页面回显实发金额
			editor.getBillCardPanel().setBodyValueAt(amountAfter, row, "defitem44");
			// 向页面回显本次预计扣税
			editor.getBillCardPanel().setBodyValueAt(tax, row, "defitem46");
		}

	}

	/**
	 * 得到实发金额，计算出本次预计扣税 和 应发金额
	 * 
	 * 应发金额 ：amount 本次预计扣税 ： defitem46 实发金额 : defitem44
	 */
	private void setValue_02(String editField, int row) {
		// 获取收支项目的值
		String incomeType = editor.getBillCardPanel().getBodyValueAt(row, "szxmid").toString();
		// 得到 defitem44 实发金额
		Double amountAfter = Double.parseDouble(editor.getBillCardPanel().getBodyValueAt(row, "defitem44").toString());
		// 判断实发金额是否合理
		if (amountAfter < 0) {
			// 如果输入负值工资 ,给出提示信息,并且清除相应表格的数据
			MessageDialog.showErrorDlg(null, "警告", "请输入正确金额,实发金额不能为负值!");
			editor.getBillCardPanel().setBodyValueAt(null, row, "amount");
			editor.getBillCardPanel().setBodyValueAt(null, row, "defitem44");
			editor.getBillCardPanel().setBodyValueAt(null, row, "defitem46");
		} else {
			// 根据收支项目的类型来计算应发金额
			Double amountBefore = 0D;
			if (incomeType != null && incomeType.contains("劳务费")) {
				amountBefore = ServiceFeeTaxUtils.serviceFeeCaculatePreTaxByAfterTax(amountAfter);
			} else if (incomeType != null && incomeType.contains("稿费")) {
				amountBefore = ServiceFeeTaxUtils.airticleFeeCaculatePreTaxByAfterTax(amountAfter);
			}
			// 本次预计扣税
			Double tax = amountBefore - amountAfter;
			// 向页面回显应发金额
			editor.getBillCardPanel().setBodyValueAt(amountBefore, row, "amount");
			// 向页面回显本次预计扣税
			editor.getBillCardPanel().setBodyValueAt(tax, row, "defitem46");
			// 表体的原币金额
			editor.getBillCardPanel().setBodyValueAt(amountBefore, row, "ybje");
		}

		int i = editor.getBillCardPanel().getRowCount();
		// 得到 amount 应发金额
		Double amount = 0D;
		Double headAmount = 0D;
		for (int j = 0; j < i; j++) {
			amount = Double.parseDouble(editor.getBillCardPanel().getBodyValueAt(j, "amount").toString());
			headAmount = headAmount + amount;
		}
		editor.getBillCardPanel().setHeadItem("total", headAmount);
		editor.getBillCardPanel().setHeadItem("bbje", headAmount);
		editor.getBillCardPanel().setHeadItem("ybje", headAmount);
	}

	/**
	 * 1.当为税前收入时，只能编辑应发金额 2.当为税后手入时，只能编辑实发金额
	 * 
	 * defitem50 劳务费类型 amount 应发金额 defitem44 实发金额
	 */
	private void check_01(String editField, int row) {
		// 得到 defitem50 劳务费类型
		String defitem50 = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem50");

		BillItem amountyingfa = editor.getBillCardPanel().getBodyItem("amount");

		BillItem amountshifa = editor.getBillCardPanel().getBodyItem("defitem44");

		if (MMValueCheck.isNotEmpty(defitem50)) {
			// 若当前行表体的劳务费类型为税前收入
			if ("税前收入".equals(defitem50)) {
				amountyingfa.setEnabled(true);// （设置可编辑，是否可编辑要设置这个。）
				amountshifa.setEnabled(false);
			} else {
				amountyingfa.setEnabled(false);// （设置可编辑，是否可编辑要设置这个。）
				amountshifa.setEnabled(true);
			}
		}

		// 应发金额
		editor.getBillCardPanel().setBodyValueAt("", row, "amount");
		// 实发金额
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem44");
		// 本次预计扣税：defitem46
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem46");
		// 增补扣税：defitem45
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem45");
		// 累计应发 defitem47
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem47");
	}

	/**
	 * 验证手机号码，身份信息
	 * 
	 * 手机号码 defitem37 证件号码 defitem42
	 * 
	 * 2.填写完表体的身份证号之后，自动带出 性别，出生日期，出生地区
	 * 
	 * 证件类型 defitem43 证件号码 defitem42 国籍区域 defitem41 性别 defitem40 出生日期 defitem39
	 * 出生国家 （地区） defitem38
	 */
	private void check_02(String editField, int row) {
		// 得到前台填写的手机号码
		String phoneNumber = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem37");
		// 得到前台填写的证件类型
		String idCardType = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem43");
		// 得到前台填写的身份证号码
		String idCardNo = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem42");

		// 若当前编辑的字段为 defitem37 手机号码
		if ("defitem37".equals(editField)) {
			if (MMValueCheck.isNotEmpty(phoneNumber)) {
				if (phoneNumber.length() != 11 || !phoneNumber.startsWith("1")) {
					MessageDialog.showErrorDlg(null, "警告", "当前行，手机号码填写有误，长度应为11位，并以1开头。");
					// 将之前填写错误的手机号码清空掉
					editor.getBillCardPanel().setBodyValueAt("", row, "defitem37");
				}
			}
		}

		// 若当前编辑的字段为 defitem42 证件号码
		if ("defitem42".equals(editField)) {
			if (MMValueCheck.isNotEmpty(idCardNo)) {
				// 当证件类型为身份证时，才做校验，并提取身份证中的信息赋值
				if ("身份证".equals(idCardType)) {
					// 校验身份证号 Start
					boolean isIdOjbk = IdCardUtil.isValidatedAllIdcard(idCardNo);
					if (!isIdOjbk) {
						MessageDialog.showErrorDlg(null, "警告", "当前行，身份证号 ： " + idCardNo + "   不合法,请核对 。");
						// 将之前填写错误的身份证号清空掉
						editor.getBillCardPanel().setBodyValueAt("", row, "defitem42");
					} else {
						// 校验身份证号通过之后，给后面几个字段赋值
						// 给国籍区域赋值，当证件类型身份证时，默认为中国
						editor.getBillCardPanel().setBodyValueAt(HRATCommonUtils.queryDefdocPk("中国", "国籍"), row, "defitem41");
						// 给性别赋值
						editor.getBillCardPanel().setBodyValueAt(IdCardUtil.getSexFromidCardNo(idCardNo), row, "defitem40");
						// 给出生日期赋值
						editor.getBillCardPanel().setBodyValueAt(IdCardUtil.getBirthDayFromidCardNo(idCardNo), row, "defitem39");
						// 给出生国家 （地区）赋值
						editor.getBillCardPanel().setBodyValueAt(HRATCommonUtils.queryDefdocPk("中国", "国籍"), row, "defitem38");
					}
				} else {
					// 当证件类型不是身份证的时候根据相应规则通过正将类型名称 idCardType和证件编码 idCardNo进行校验，
					boolean flag = HRATCommonUtils.judgeIDNumberIsLegal(idCardType, idCardNo);
					if (flag) {
						MessageDialog.showErrorDlg(null, "警告", "当前行，证件号码 ： " + idCardNo + "   不合法,请核对 。");
						// 将之前填写错误的身份证号清空掉
						editor.getBillCardPanel().setBodyValueAt("", row, "defitem42");
					}
				}
			} else {
				// 给国籍赋值为空
				editor.getBillCardPanel().setBodyValueAt(null, row, "defitem41");
				// 给性别赋值为空
				editor.getBillCardPanel().setBodyValueAt(null, row, "defitem40");
				// 给出生日期赋值为空
				editor.getBillCardPanel().setBodyValueAt("", row, "defitem39");
				// 给出生国家 （地区）赋值为空
				editor.getBillCardPanel().setBodyValueAt(null, row, "defitem38");
			}
		}
	}

	/**
	 * 是否已现场发放：参照，选择Y或N，如果选择N，则银行类别、开户银行、银行账户需要必填。 是否已现场发放 defitem36 银行类别
	 * defitem35 开户银行 defitem34 银行卡号 defitem33
	 */
	private void check_03(String editField, int row) {
		// 得到前台选择的 是否现场发放 字段值
		String isSitetoExtend = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem36");

		BillItem bankType = editor.getBillCardPanel().getBodyItem("defitem35");
		BillItem bankName = editor.getBillCardPanel().getBodyItem("defitem34");
		BillItem bankNo = editor.getBillCardPanel().getBodyItem("defitem33");

		if ("否".equals(isSitetoExtend)) {
			bankType.setNull(true);
			bankName.setNull(true);
			bankNo.setNull(true);
		} else {
			bankType.setNull(false);
			bankName.setNull(false);
			bankNo.setNull(false);
		}
	}

	/**
	 * @Title: check_04
	 * @Description: 填写姓名时校验此身份证号第一次填写报销单时对应的姓名是否一样
	 * 
	 *               姓名 defitem48 表体 表名：er_busitem
	 * @param @param editField
	 * @param @param row 参数
	 * @return void 返回类型
	 * @throws
	 */
	private void check_04(String editField, int row) {
		// 得到前台填写的身份证号码
		String idCardNo = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem42");
		// 得到前台填写的姓名
		String name = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem48");

		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		SqlBuilder sql = new SqlBuilder();

		// 当填写的身份证号和姓名都不为空的时候执行校验
		if (HRATCommonUtils.isNotEmpty(idCardNo) && HRATCommonUtils.isNotEmpty(name)) {
			sql.append("    SELECT tb_b.defitem48 ");
			sql.append("      FROM er_busitem tb_b ");
			sql.append(" LEFT JOIN er_bxzb tb_h ");
			sql.append(" 		ON ");
			sql.append(" tb_h.pk_jkbx = tb_b.pk_jkbx ");
			sql.append(" 	 WHERE ");
			sql.append("tb_h.djlxbm", this.TRANSTYPE);
			sql.append(" 	   AND ");
			sql.append(" tb_b.defitem42 ", idCardNo);
			sql.append("  ORDER BY tb_b.ts ASC ");

			try {
				Object result = queryBS.executeQuery(sql.toString(), new ColumnProcessor());

				if (HRATCommonUtils.isNotEmpty(result)) {
					String nameOril = result.toString();
					if (!nameOril.equals(name)) {
						MessageDialog.showErrorDlg(null, "警告", "当前行,姓名 ： " + name + "  与原始填报人姓名不匹配，请核对后填写！");
						// 将之前填写错误的身份证号清空掉
						editor.getBillCardPanel().setBodyValueAt("", row, "defitem48");
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: check_05
	 * @Description: 切换证件类型,相应表格内容清空
	 * 
	 *               证件类型 defitem43 表体 表名：er_busitem
	 * @param @param editField
	 * @param @param row 参数
	 * @return void 返回类型
	 * @throws
	 */
	private void check_05(String editField, int row) {
		String idType = editor.getBillCardPanel().getBodyValueAt(row, "defitem43").toString();
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem42");
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem41");
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem40");
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem39");
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem38");
	}

	/**
	 * 选择收支项目之后才能编辑对应的金额，触发对应的公式
	 * 
	 * @param editField
	 * @param row
	 *            实发金额：defitem44 应发金额：amount 本次预计扣税：defitem46 增补扣税：defitem45
	 *            累计应发 defitem47
	 */
	private void check_06(String editField, int row) {
		// 应发金额
		editor.getBillCardPanel().setBodyValueAt("", row, "amount");
		// 实发金额
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem44");
		// 本次预计扣税：defitem46
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem46");
		// 增补扣税：defitem45
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem45");
		// 累计应发 defitem47
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem47");
	}

	protected void setHeadValue(String key, Object value) {
		if (getBillCardPanel().getHeadItem(key) != null)
			getBillCardPanel().getHeadItem(key).setValue(value);
	}

	private BillCardPanel getBillCardPanel() {
		return this.editor.getBillCardPanel();
	}

}
