package nc.ui.erm.billpub.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Pattern;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.config.BDModeSelectedVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.util.BDModeManager;
import nc.vo.util.BDPKLockUtil;
import nc.vo.util.bizlock.BizlockDataUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.math.NumberUtils;

/**
 * �������ϵͳ����������
 * 
 * @author yaojing
 * 
 */
public class HRATCommonUtils extends ArrayUtils {

	/**
	 * @Title: judgeIDNumberIsLegal
	 * @Description: �жϲ�ͬ�����֤���� �Ƿ�Ϸ�
	 * @param @param cardType
	 * @param @param idCardNo
	 * @param @return ����
	 * @return boolean ��������
	 * 
	 *         ֤������Ϊ�й����գ�У����λ����ĸ����Ҫ�������֡�
	 *         ֤������Ϊ�۰ľ��������ڵ�ͨ��֤ʱ��У���1λΪ��ĸ����H����M������2λ����11λΪ���֡�
	 *         ֤������Ϊ̨�����������½ͨ��֤ʱ��У��Ϊ�����֡� ֤������Ϊ�������ʱ����У�顣
	 * 
	 * @throws
	 */
	public static boolean judgeIDNumberIsLegal(String cardType, String idCardNo) {
		// Ϊ������
		String digitalRegex = "^[0-9]*$";
		// ��������
		String isContainDigitalRegex = ".*\\d+.*";
		// ����ĸ��ͷ
		String isStartWithLetter = "^[a-zA-z].*";

		// �Ƿ�Ϸ���ʶ����ʼ��Ϊfalse
		boolean flag = true;

		switch (cardType) {

		case "�й�����":
			Pattern pattern1 = Pattern.compile(isStartWithLetter);
			// ��һλ����ĸ
			if (pattern1.matcher(idCardNo).matches()) {
				Pattern p = Pattern.compile(isContainDigitalRegex);
				// ��������
				if (p.matcher(idCardNo).matches()) {
					flag = false;
				}
			}
			break;

		case "�۰ľ��������ڵ�ͨ��֤":
			// ��H����M��ͷ
			if (idCardNo.startsWith("H") || idCardNo.startsWith("M")) {
				// ���ҽ�ȡ���沿�ֵ����о�Ϊ����
				String splitStr = idCardNo.substring(1);
				Pattern pattern2 = Pattern.compile(digitalRegex);
				if (pattern2.matcher(splitStr).matches()) {
					flag = false;
				}
			}
			break;

		case "̨�����������½ͨ��֤":
			// У��Ϊ������
			Pattern pattern2 = Pattern.compile(digitalRegex);
			if (pattern2.matcher(idCardNo).matches()) {
				flag = false;
			}
			break;

		case "�������":
			flag = false;
			break;

		}
		return flag;
	}

	/**
	 * @Title: queryDefdocByName
	 * @Description: �����Զ�������б������ �Լ�������Ŀ�����ƻ�ȡ���յ�����
	 * @param @param listName
	 * @param @param defdocName
	 * @param @return ����
	 * @return String ��������
	 * @throws
	 */
	public static String queryDefdocByName(String listName, String defdocName) {
		SqlBuilder sql = new SqlBuilder();

		sql.append("  SELECT a.pk_defdoc " + " FROM bd_defdoc a "
				+ " LEFT JOIN " + "bd_defdoclist b "
				+ "ON a.pk_defdoclist = b.pk_defdoclist " + "WHERE ");
		sql.append(" b.name ", listName);
		sql.append(" AND ");
		sql.append(" a.name ", defdocName);

		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		String result = null;
		try {
			result = (String) iUAPQueryBS.executeQuery(sql.toString(),
					new ColumnProcessor());// ���ص�һ���
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ͨ���õ�������������ѯbom�е���Ϣ
	 * 
	 * @param pk_material
	 * @return
	 */
	public static String queryBomByPk_material(String pk_material) {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" hcmaterialid ", pk_material);
		sql.append(" and dr ", 0);
		String[] names = new String[] { "hvdef1" };
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<BomVO> list = new ArrayList<BomVO>();
		try {
			list = (List<BomVO>) queryBS.retrieveByClause(BomVO.class,
					sql.toString(), names);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		if (HRATCommonUtils.isNotEmpty(list) && list.size() == 1) {
			return list.get(0).getAttributeValue("hvdef1").toString();
		} else {
			return null;
		}
	}

	/**
	 * ���ݿͻ�������������ѯ�ͻ�����
	 * 
	 * @param pk_customer
	 * @return
	 */
	public static String queryCustByPK(String pk_customer) {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" pk_customer ", pk_customer);
		sql.append(" and dr ", 0);
		String[] names = new String[] { "name" };
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<CustomerVO> list = new ArrayList<CustomerVO>();
		try {
			list = (List<CustomerVO>) queryBS.retrieveByClause(
					CustomerVO.class, sql.toString(), names);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		if (HRATCommonUtils.isNotEmpty(list) && list.size() == 1) {
			return (String) list.get(0).getAttributeValue("name");
		} else {
			return null;
		}
	}

	/**
	 * ͨ����������λ������ѯ�����е���������λ
	 * 
	 * @param pk_measdoc
	 * @return
	 * @throws BusinessException
	 */
	public static String queryMeasdocBypk(String pk_measdoc)
			throws BusinessException {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" pk_measdoc ", pk_measdoc);
		sql.append(" and dr ", 0);
		String[] names = new String[] { MeasdocVO.NAME };
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<MeasdocVO> list = (List<MeasdocVO>) queryBS.retrieveByClause(
				MeasdocVO.class, sql.toString(), names);

		if (HRATCommonUtils.isNotEmpty(list)) {
			return list.get(0).getName();
		} else {
			return null;
		}
	}

	/**
	 * �б�����в�����ʾ����
	 * 
	 * @param arrs
	 * @param pk
	 * @return
	 */
	public static String findRefNameByPKInVector(Vector arrs, String pk) {
		if (HRATCommonUtils.isNotEmpty(pk)) {
			Vector childernarr = null;
			for (int i = 0; i < arrs.size(); i++) {
				childernarr = (Vector) arrs.get(i);
				if (pk.equals(childernarr.get(2))) {
					return (String) childernarr.get(1);
				}
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * ͨ���������� ����һ����0 ��ʼ�������� ������ ���ⵥ �л��������ɱ�������
	 */
	public static int[] generateArrByAmount(int amount) {
		// �½�һ������Ϊamount������
		int[] resultArr = new int[amount];
		// Ĭ�ϴ���������������0
		if (HRATCommonUtils.isNotEmpty(amount)) {
			// ѭ�����丳ֵ
			for (int i = 0; i < resultArr.length; i++) {
				resultArr[i] = i;
			}
		} else {
			ExceptionUtils.wrappBusinessException("�������ʧ��");
		}
		return resultArr;
	}

	/**
	 * ����ʱ����ļ�������
	 * 
	 * @param vos
	 * @throws BusinessException
	 */
	public static void insertlockOperate(SuperVO... vos)
			throws BusinessException {
		BizlockDataUtil.lockDataByBizlock(vos);
	}

	/**
	 * �޸�ʱ����ļ�������
	 * 
	 * @param vos
	 * @throws BusinessException
	 */
	public static void updatelockOperate(SuperVO... vos)
			throws BusinessException {
		BDPKLockUtil.lockSuperVO(vos);
		BizlockDataUtil.lockDataByBizlock(vos);
	}

	/**
	 * ɾ��ʱ����ļ�������
	 * 
	 * @param vos
	 * @throws BusinessException
	 */
	public static void deletelockOperate(SuperVO... vos)
			throws BusinessException {
		BDPKLockUtil.lockSuperVO(vos);
	}

	/**
	 * �����������������ۺ�VO���ɣ۱�ͷ��������ͷVO�ݵ�Map
	 * 
	 * @param @param vos
	 * @param @return
	 * @return Map<String,E>
	 * @throws
	 */
	public static <T extends ISuperVO> Map<String, T> toPrimaryKeyHeadMap(
			SuperVO[] vos) {
		if (isEmpty(vos)) {
			return null;
		}
		Map<String, T> keyMap = new LinkedHashMap<String, T>();
		for (SuperVO vo : vos) {
			if (isEmpty(vo)) {
				continue;
			}
			String id = vo.getPrimaryKey();
			if (HRATCommonUtils.isEmpty(id)) {
				keyMap.put(id, (T) vo);
			}
		}
		return keyMap;
	}

	/**
	 * @param <T>
	 * @Title: getMaterialCache
	 * @Description: �õ��������ϵĻ���
	 * @param @return Map<������������������>
	 * @param @throws BusinessException ����
	 * @return Map<String,String> ��������
	 * @throws ���ڴ�����Ŀ���ϲ�����д��ԭ��
	 *             Ŀǰ�������ֶ����������ϲ����к�̨��������ϱ��е�pk_source �����ɽӿ�ͬ������������ֱ����pk_material
	 */
	public static List<MaterialVO> isContainMaterial(String[] materialcode)
			throws BusinessException {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" pk_material ", materialcode);
		sql.append(" or pk_source ", materialcode);
		sql.append(" and enablestate ", 2);// ����Ϊ����״̬
		sql.append(" and dr ", 0);
		// def4�д�������۶�����������ñ�ǣ��������������ӡ����
		String[] names = new String[] { MaterialVO.PK_MATERIAL,
				MaterialVO.NAME, MaterialVO.CODE, MaterialVO.DEF4,
				MaterialVO.DEF6, MaterialVO.MATERIALSPEC,
				MaterialVO.PK_MEASDOC, MaterialVO.MATERIALTYPE };// materialtype
																	// ��������
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<MaterialVO> list = (List<MaterialVO>) queryBS.retrieveByClause(
				MaterialVO.class, sql.toString(), names);

		return list;
	}

	public static List<MaterialVO> queryMaterialByPk(String[] pk_material,
			String[] attr) throws BusinessException {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" pk_material ", pk_material);
		sql.append(" or pk_source ", pk_material);
		sql.append(" and enablestate ", 2);// ����Ϊ����״̬
		sql.append(" and dr ", 0);
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<MaterialVO> list = (List<MaterialVO>) queryBS.retrieveByClause(
				MaterialVO.class, sql.toString(), attr);

		return list;
	}

	/**
	 * ͨ��value���map�е�key
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList valueGetKey(Map map, String value) {
		Set set = map.entrySet();
		ArrayList arr = new ArrayList<>();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue().equals(value)) {
				String s = (String) entry.getKey();
				arr.add(s);
			}
		}
		return arr;
	}

	/**
	 * ��vosת����ָ���ֶ�ֵ��pkֵ��Ϊkey��map
	 * 
	 * @param <T>
	 * @param voList
	 * @return Map<fieldValue,T>
	 */
	public static Map<String, String> toKeyVOMap(SuperVO[] vos, String field) {
		if (HRATCommonUtils.isEmpty(vos)) {
			return null;
		}
		Map<String, String> keyMap = new LinkedHashMap<String, String>();
		for (SuperVO t : vos) {
			if (MMValueCheck.isEmpty(t)) {
				continue;
			}
			ISuperVO vo = null;
			if (t instanceof ISuperVO) {
				vo = ISuperVO.class.cast(t);
			}
			if (MMValueCheck.isEmpty(vo)) {
				continue;
			}
			Object value = null;
			if (MMValueCheck.isNotEmpty(field)) {
				value = vo.getAttributeValue(field);
			} else {
				value = vo.getPrimaryKey();
			}
			if (MMValueCheck.isNotEmpty(value)) {
				keyMap.put(vo.getPrimaryKey(), (String) value);
			}
		}
		return keyMap;
	}

	/**
	 * ͨ������������������������ת��
	 * 
	 * @param name
	 * @return
	 */
	public static String queryBankByName(String name) {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pk_bankdoc FROM bd_bankdoc  WHERE name = '" + name
				+ "'");
		Object result = null;
		try {
			result = queryBS
					.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ����֧��Ŀ��������������ת��
	 * 
	 * @param name
	 * @return
	 */
	public static String queryInoutbusiclassByName(String name) {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer();
		
		if(HRATCommonUtils.isNotEmpty(name)){
			// �ж�name���������� ��֧��Ŀ����
			if (name.contains("�����") || name.contains("���")) {
				// ��֧��Ŀ����
				sql.append(" SELECT pk_inoutbusiclass FROM bd_inoutbusiclass  WHERE name = '"+ name + "'");
			} else {
				// ����
				sql.append(" SELECT name FROM bd_inoutbusiclass  WHERE pk_inoutbusiclass = '"+ name + "'");
			}
			Object result = null;
			try {
				result = queryBS
						.executeQuery(sql.toString(), new ColumnProcessor());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			if (result == null){
				return null;
			}
			return result.toString();
		}
		return null;
	}

	/**
	 * ͨ�����������������������ת��
	 * 
	 * @param name
	 * @return
	 */
	public static String queryBankTypeByName(String name) {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pk_banktype FROM bd_banktype  WHERE name = '"
				+ name + "'");
		Object result = null;
		try {
			result = queryBS
					.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (result == null)
			return "";
		return result.toString();
	}
	
	
	/**
	  * ͨ���Զ��嵵����Ϣ�õ����� 
	  * 
	  * @param materialid
	  * @return
	  * @throws BusinessException
	  */
	public static String queryNameByDefdocPk(String pk_defdoc,
			String doclistname) {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT name FROM bd_defdoc where pk_defdoc = '"
				+ pk_defdoc
				+ "'  and pk_defdoclist in  (SELECT pk_defdoclist FROM bd_defdoclist where name='"
				+ doclistname + "' )");
		Object result = null;
		try {
			result = queryBS
					.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ���Զ��嵵����Ϣ�õ����� defdoc-������ϸ��Ϣ�ı�������� defdoclist-���������б�ı��������
	 * 
	 * @param materialid
	 * @return
	 * @throws BusinessException
	 */
	public static String queryDefdocPk(String defdoc, String defdoclist) {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT pk_defdoc FROM bd_defdoc where ( name='" + defdoc
				+ "' or code='" + defdoc + "' ) ");
		sql.append(" and pk_defdoclist in ");
		sql.append(" (SELECT pk_defdoclist FROM bd_defdoclist where name='"
				+ defdoclist + "' or code='" + defdoclist + "') ");
		Object result = null;
		try {
			result = queryBS
					.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ���û�id��ѯ��ǰ�û�����֯��Ϣ
	 * 
	 * */
	public static String queryPkorg(String pk_loginUser)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_ORG FROM SM_USER where  CUSERID='"
				+ pk_loginUser + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ��ѯ���������ݿ��е������ж�����
	 * 
	 * */
	public static int queryLmCount() throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT COUNT(*) FROM  CRM_LIFELIMITS ");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return 0;
		return Integer.parseInt(result.toString());
	}

	/**
	 * ͨ����ǰ������ѯ�Ƿ���ڸ�������
	 * 
	 * */
	public static Object queryPkServicePlan(String pk_serviceplan)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT * FROM CRM_SERVICEPLAN where  pk_serviceplan='"
				+ pk_serviceplan + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());

		return HRATCommonUtils.isNotEmpty(result) ? result : null;
	}

	/**
	 * ͨ��������������Ʋ�ѯ��Ӧ������pkֵ
	 * 
	 * */
	public static String queryPkByMaterialname(String materialname)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_MATERIAL FROM BD_MATERIAL_V WHERE NAME = '"
				+ materialname + "'");
		String pk_material = (String) queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());

		return HRATCommonUtils.isNotEmpty(pk_material) ? pk_material : null;
	}

	/**
	 * ͨ�����������ֲ�ѯ����
	 * 
	 * */
	public static String queryPkshipclub(String name) throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_SHIPCLUB FROM CRM_SHIPCLUB where  NAME='" + name
				+ "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ������������ѯ��������
	 * 
	 * */
	public static String queryNameByMaterial(String materialcode)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT NAME FROM BD_MATERIAL_V WHERE PK_MATERIAL ='"
				+ materialcode + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ������������ѯ����ͺ�
	 * 
	 * */
	public static String queryModelByMaterial(String materialcode)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT MATERIALSPEC FROM BD_MATERIAL_V WHERE PK_MATERIAL ='"
				+ materialcode + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ������������ѯ���ű���
	 * 
	 * */
	public static String queryCodeByProjectno(String projectno)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PROJECTCODE FROM CRM_PROJECTNO WHERE PK_PROJECTNO ='"
				+ projectno + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ�����۶���������ѯ�õ��ݵļ��ź���֯
	 * 
	 * */
	public static String queryGroupByPksalesorder(String pk_salesorder)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT PK_GROUP FROM CRM_SALESORDER WHERE PK_SALESORDER = '"
				+ pk_salesorder + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	public static String queryOrgByPksalesorder(String pk_salesorder)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT PK_ORG FROM CRM_SALESORDER WHERE PK_SALESORDER = '"
				+ pk_salesorder + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ�����ű����ѯ��������
	 * 
	 * */
	public static String queryPkByProjectcode(String projectcode)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_PROJECTNO FROM CRM_PROJECTNO WHERE PROJECTCODE ='"
				+ projectcode + "'");
		Object pk_projectno = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (pk_projectno == null)
			return "";
		return pk_projectno.toString();
	}

	/**
	 * ͨ������������ѯ��������
	 * 
	 * */
	public static String queryNameByProjectcode(String projectno)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PROJECTCODE FROM CRM_PROJECTNO WHERE PK_PROJECTNO ='"
				+ projectno + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ���������Ʋ�ѯ��������
	 * 
	 * */
	public static String queryPKByProjectname(String projectname)
			throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_PROJECTNO FROM CRM_PROJECTNO WHERE PROJECTNAME ='"
				+ projectname + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ���������ֲ�ѯ����
	 * 
	 * */
	public static String queryPkshiptype(String name) throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_SHIPTYPE FROM CRM_SHIPTYPE where  NAME='" + name
				+ "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ���������ֲ�ѯ����
	 * 
	 * */
	public static String queryPkdept(String name) throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_DEPT FROM ORG_DEPT where  NAME='" + name + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ���û�����������
	 * 
	 * */
	public static String queryCustNameByPK(String pk) throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT name FROM BD_CUSTOMER where  pk_customer='" + pk
				+ "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ���Ñ����ֲ�ѯ����
	 * 
	 * */
	public static String queryPkcustomer(String name) throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_customer FROM BD_CUSTOMER where  NAME='" + name
				+ "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ͨ���������ֲ�ѯ����
	 * 
	 * */
	public static String queryPkshipowner(String name) throws BusinessException {
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT PK_SHIPOWNER FROM CRM_SHIPOWNER where  NAME='"
				+ name + "'");
		Object result = queryBS.executeQuery(sql.toString(),
				new ColumnProcessor());
		if (result == null)
			return "";
		return result.toString();
	}

	/**
	 * ��string����ת��Ϊ���Ÿ������ַ���
	 * 
	 * @param ids
	 * @return
	 */
	public static String StringarrayToString(String[] ids) {
		StringBuffer idsStr = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				idsStr.append(",");
			}
			idsStr.append(ids[i]);
		}
		return idsStr.toString();
	}

	/**
	 * ��string����ת��Ϊsql��in���������Ÿ������ַ�����
	 * 
	 * @param ids
	 * @return
	 */
	public static String StringarrayToInsql(String[] ids) {
		StringBuffer idsStr = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				idsStr.append(",");
			}
			idsStr.append("'").append(ids[i]).append("'");
		}
		return idsStr.toString();
	}

	public static boolean isNotEmpty(Object value) {
		return !HRATCommonUtils.isEmpty(value);
	}

	/**
	 * �������Ƿ�Ϊ�ա�
	 * 
	 * @return boolean ��������ֵΪnull������true��
	 *         ���value������ΪString������value.length()Ϊ0������true��
	 *         ���value������ΪObject[]������value.lengthΪ0������true��
	 *         ���value������ΪCollection������value.size()Ϊ0������true��
	 *         ���value������ΪDictionary������value.size()Ϊ0������true�� ���򷵻�false��
	 * 
	 * @param value
	 *            �����ֵ��
	 */
	public static boolean isEmpty(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof String && ((String) value).trim().length() <= 0) {
			return true;
		}
		if (value instanceof Object[] && ((Object[]) value).length <= 0) {
			return true;
		}
		if (value instanceof Collection && ((Collection<?>) value).size() <= 0) {
			return true;
		}
		if (value instanceof Map && ((Map<?, ?>) value).size() <= 0) {
			return true;
		}
		if (value instanceof StringBuilder) {
			return value.toString().trim().length() <= 0;
		}
		if (value instanceof StringBuffer) {
			return value.toString().trim().length() <= 0;
		}
		if (value instanceof SqlBuilder) {
			return value.toString().trim().length() <= 0;
		}
		return false;
	}

	/**
	 * ����������������Object����ת��Ϊ��Ҫ���͵�����
	 * 
	 * @param objs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] convertArrayType(Object[] objs) {
		if (isEmpty(objs)) {
			return null;
		}
		T[] convertArray = (T[]) Array.newInstance(objs[0].getClass(),
				objs.length);
		System.arraycopy(objs, 0, convertArray, 0, objs.length);
		return convertArray;
	}

	/**
	 * ���ݲ�ͬ����ת��������
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> T[] getObjs(T obj) {
		if (isEmpty(obj)) {
			return null;
		}
		T[] objs = null;
		if (obj.getClass().isArray()) {
			objs = (T[]) obj;
		} else {
			objs[0] = obj;
		}
		return objs;
	}

	/**
	 * ��������������������ת��Ϊ���飬��������������򷵻�size=1������
	 * 
	 * @param @param obj
	 * @param @return
	 * @return T[]
	 * @throws
	 */
	public static <T> T[] convertToArray(T obj) {
		T[] array = getObjs(obj);
		if (isEmpty(array)) {
			return null;
		}
		T[] convertArray = (T[]) Array.newInstance(array[0].getClass(),
				array.length);
		System.arraycopy(array, 0, convertArray, 0, array.length);
		return convertArray;
	}

	/**
	 * �Ƿ���ڹܿ�ģʽ����
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean isManagerMode(Class<?> className) {
		try {
			IBean ibean = MDBaseQueryFacade.getInstance()
					.getBeanByFullClassName(className.getName());
			BDModeSelectedVO vo = BDModeManager.getInstance()
					.getBDModeSelectedVOByMDClassID(ibean.getID());
			return isNotEmpty(vo);
		} catch (MetaDataException e) {
			Logger.warn("��ȡ�Ƿ��йܿ�ģʽ�����쳣����Ϊû�йܿ�ģʽ����", e);
		}
		return false;
	}

	/**
	 * ����һ�����͵�ʵ��
	 * 
	 * @param voclass
	 * @return
	 */
	public static <T> T construct(Class<T> voclass) {
		T instance = null;
		try {
			instance = voclass.newInstance();
		} catch (InstantiationException ex) {
			ExceptionUtils.wrappException(ex);
		} catch (IllegalAccessException ex) {
			ExceptionUtils.wrappException(ex);
		}
		return instance;
	}

	/**
	 * ����ȫ·������һ����
	 * 
	 * @param voClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String voClass) {
		T instance = null;
		try {
			instance = (T) Class.forName(voClass);
		} catch (ClassNotFoundException e) {
			ExceptionUtils.wrappBusinessException("�Ҳ���������Ϊ��" + voClass
					+ " �����ͣ�");
		}
		return instance;
	}

	/**
	 * ��ȿ�¡
	 * 
	 * @param obj
	 *            Ҫ��¡�Ķ���
	 * 
	 * @return ��¡�������¶���
	 */
	public static Object deepClone(Serializable obj) {
		byte[] bytes = serialize(obj);
		Object value = deserialize(bytes);
		return value;
	}

	public static Object deserialize(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("The byte[] must not be null");
		}
		Object value = null;
		ObjectInputStream in = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try {
			in = new ObjectInputStream(bais);
			value = in.readObject();
		} catch (ClassNotFoundException e) {
			ExceptionUtils.wrappException(e);
		} catch (IOException e) {
			ExceptionUtils.wrappException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				ExceptionUtils.wrappException(e);
			}
		}
		return value;
	}

	public static byte[] serialize(Serializable obj) {
		if (obj == null) {
			throw new IllegalArgumentException(
					"The Serializable must not be null");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(obj);
		} catch (IOException e) {
			ExceptionUtils.wrappException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				ExceptionUtils.wrappException(e);
			}
		}
		return baos.toByteArray();
	}

	/**
	 * �����������������ۺ�VO���ɣ۱�ͷ��������ͷVO�ݵ�Map
	 * 
	 * @param @param vos
	 * @param @return
	 * @return Map<String,E>
	 * @throws
	 */
	public static <T extends ISuperVO> Map<String, T> toPrimaryKeyHeadMap(
			IBill[] vos) {
		if (isEmpty(vos)) {
			return null;
		}
		Map<String, T> keyMap = new LinkedHashMap<String, T>();
		for (IBill vo : vos) {
			if (isEmpty(vo) || isEmpty(vo.getParent())) {
				continue;
			}
			String id = vo.getPrimaryKey();
			if (isNotEmpty(id)) {
				keyMap.put(id, (T) vo.getParent());
			}
		}
		return keyMap;
	}

	/**
	 * �����������������ۺ�VO���ɣ۱�������������VO�ݵ�Map
	 * 
	 * @param @param vos
	 * @param @return
	 * @return Map<String,E>
	 * @throws
	 */
	public static <K, E extends ISuperVO> Map<K, E> toPrimaryKeyItemMap(
			AbstractBill[] vos) {
		return toFieldItemMap(vos, null);
	}

	/**
	 * �����������������ۺ�VO���ɣ۱���ָ���ֶΣ�����VO�ݵ�Map
	 * 
	 * @param @param vos
	 * @param @return
	 * @return Map<String,E>
	 * @throws
	 */
	public static <K, E extends ISuperVO> Map<K, E> toFieldItemMap(
			AbstractBill[] vos, String field) {
		return toFieldItemMap(vos, field, null);
	}

	/**
	 * �����������������ۺ�VO���ɣ۱�������������VO�ݵ�Map
	 * 
	 * @param @param vos
	 * @param @param className
	 * @param @return
	 * @return Map<String,E>
	 * @throws
	 */
	public static <K, E extends ISuperVO> Map<K, E> toFieldItemMap(
			AbstractBill[] vos, String field, Class<E> clazz) {
		if (isEmpty(vos)) {
			return null;
		}
		Map<K, E> keyMap = new LinkedHashMap<K, E>();
		for (AbstractBill vo : vos) {
			if (isEmpty(vo)) {
				continue;
			}
			ISuperVO[] bvos = null;
			if (isNotEmpty(clazz)) {
				bvos = vo.getChildren(clazz);
			} else {
				bvos = (ISuperVO[]) vo.getChildrenVO();
			}
			if (isEmpty(bvos)) {
				continue;
			}
			for (ISuperVO bvo : bvos) {
				if (isEmpty(bvo)) {
					continue;
				}
				Object value = null;
				if (isNotEmpty(field)) {
					value = bvo.getAttributeValue(field);
				} else {
					value = bvo.getPrimaryKey();
				}
				if (isNotEmpty(value)) {
					keyMap.put((K) value, (E) bvo);
				}
			}
		}
		return keyMap;
	}

	/**
	 * ��þۺ�vo������vos
	 * 
	 * @param @param aggvos
	 * @param @return
	 * @return SuperVO[]
	 * @throws
	 */
	public static <E extends ISuperVO> E[] toParentVOs(IBill[] vos) {
		if (isEmpty(vos)) {
			return null;
		}
		List<E> voList = new ArrayList<E>();
		for (IBill vo : vos) {
			if (isEmpty(vo) || isEmpty(vo.getParent())) {
				continue;
			}
			voList.add((E) vo.getParent());
		}
		return convertArrayType(voList.toArray());
	}

	/**
	 * ��þۺ�vo���ӱ�vos
	 * 
	 * @param @param aggvos
	 * @param @return
	 * @return SuperVO[]
	 * @throws
	 */
	public static <E extends ISuperVO> E[] toChildrenVOs(AbstractBill[] vos) {
		return toChildrenVOs(vos, null);
	}

	/**
	 * ��þۺ�vo���ӱ�vos
	 * 
	 * @param @param aggvos
	 * @param @return
	 * @return SuperVO[]
	 * @throws
	 */
	public static <E extends ISuperVO> E[] toChildrenVOs(AbstractBill[] vos,
			Class<E> clazz) {
		if (isEmpty(vos)) {
			return null;
		}
		List<E> bvoList = new ArrayList<E>();
		for (AbstractBill vo : vos) {
			if (isEmpty(vo)) {
				continue;
			}
			ISuperVO[] bvos = null;
			if (isNotEmpty(clazz)) {
				bvos = vo.getChildren(clazz);
			} else {
				bvos = (ISuperVO[]) vo.getChildrenVO();
			}
			if (isEmpty(bvos)) {
				continue;
			}
			for (ISuperVO bvo : bvos) {
				if (isEmpty(bvo)) {
					continue;
				}
				bvoList.add((E) bvo);
			}
		}
		return convertArrayType(bvoList.toArray());
	}

	/**
	 * ����VO��ȡpks����
	 * 
	 * @param vos
	 * @param feildName
	 * @return
	 */
	public static <T> String[] getPrimaryKeys(T[] vos) {
		return getFieldValues(vos, null);
	}

	/**
	 * ����VO��ȡĳ�����ݼ���
	 * 
	 * @param vos
	 * @param feildName
	 * @return
	 */
	public static <T> String[] getFieldValues(T[] vos, String field) {
		Map<Object, T> keyMap = toKeyVOMap(vos, field);
		return isNotEmpty(keyMap) ? keyMap.keySet().toArray(new String[0])
				: null;
	}

	/**
	 * ����VO��ȡĳ�����ݼ���
	 * 
	 * @param vos
	 * @param feildName
	 * @return
	 */
	public static <T> Object[] getAttributeValues(T[] vos, String field) {
		if (isEmpty(vos)) {
			return null;
		}
		List<Object> objs = new ArrayList<Object>();
		for (T t : vos) {
			if (isEmpty(t)) {
				continue;
			}
			ISuperVO vo = null;
			if (t instanceof ISuperVO) {
				vo = ISuperVO.class.cast(t);
			} else if (t instanceof AbstractBill) {
				vo = AbstractBill.class.cast(t).getParent();
			} else {
				ExceptionUtils
						.wrappBusinessException("Ŀǰֻ֧��SuperVO��AbstractBill�ṹ������");
			}
			if (isEmpty(vo)) {
				continue;
			}
			Object value = null;
			if (isNotEmpty(field)) {
				value = vo.getAttributeValue(field);
			} else {
				value = vo.getPrimaryKey();
			}
			if (isNotEmpty(value)) {
				objs.add(value);
			}
		}
		return isNotEmpty(objs) ? objs.toArray() : null;
	}

	/**
	 * ��vosת����PKֵΪkey��map
	 * 
	 * 
	 * @param <T>
	 * @param voList
	 * @return Map<fieldValue,T>
	 */
	public static <K, T> Map<K, T> toPrimaryKeyMap(T[] vos) {
		return toKeyVOMap(vos, null);
	}

	/**
	 * ��vosת�����ֶ�ֵ��PKֵΪkey��map
	 * 
	 * 
	 * @param <T>
	 * @param voList
	 * @return Map<fieldValue,T>
	 */
	public static <K, T> Map<K, T> toKeyVOMap(T[] vos, String field) {
		if (isEmpty(vos)) {
			return null;
		}
		Map<K, T> keyMap = new LinkedHashMap<K, T>();
		for (T t : vos) {
			if (isEmpty(t)) {
				continue;
			}
			ISuperVO vo = null;
			if (t instanceof ISuperVO) {
				vo = ISuperVO.class.cast(t);
			} else if (t instanceof AbstractBill) {
				vo = AbstractBill.class.cast(t).getParent();
			} else {
				ExceptionUtils
						.wrappBusinessException("Ŀǰֻ֧��SuperVO��AbstractBill�ṹ������");
			}
			if (isEmpty(vo)) {
				continue;
			}
			Object value = null;
			if (isNotEmpty(field)) {
				value = vo.getAttributeValue(field);
			} else {
				value = vo.getPrimaryKey();
			}
			if (isNotEmpty(value)) {
				keyMap.put((K) value, (T) t);
			}
		}
		return keyMap;
	}

	/**
	 * ��vosת����ָ���ֶ�ֵΪkey��map
	 * 
	 * @param <T>
	 * @param voList
	 * @param field
	 *            �����ֶ�
	 * 
	 * @return Map<fieldValue,List<T>>
	 */
	public static <K, T> Map<K, List<T>> toKeyListVOMap(T[] vos, String field) {
		return toKeyListVOMap(vos, new String[] { field });
	}

	/**
	 * ��voListת����ָ���ֶ�ֵΪkey��map
	 * 
	 * @param <T>
	 * @param voList
	 * @param field
	 *            �����ֶ�
	 * 
	 * @return Map<fieldValue,List<T>>
	 */
	public static <K, T> Map<K, List<T>> toKeyListVOMap(T[] vos, String[] fields) {
		if (isEmpty(vos)) {
			return null;
		}
		Map<K, List<T>> keyMap = new LinkedHashMap<K, List<T>>();
		for (T t : vos) {
			if (isEmpty(t)) {
				continue;
			}
			ISuperVO vo = null;
			if (t instanceof ISuperVO) {
				vo = ISuperVO.class.cast(t);
			} else if (t instanceof AbstractBill) {
				vo = AbstractBill.class.cast(t).getParent();
			} else {
				ExceptionUtils
						.wrappBusinessException("Ŀǰֻ֧��SuperVO��AbstractBill�ṹ������");
			}
			if (isEmpty(vo)) {
				continue;
			}
			StringBuffer buffer = new StringBuffer("");
			for (String field : fields) {
				Object value = vo.getAttributeValue(field);
				buffer.append(isNotEmpty(value) ? value : "");
			}
			if (isEmpty(buffer)) {
				continue;
			}
			String key = buffer.toString().trim();
			if (keyMap.containsKey(key)) {
				keyMap.get(key).add((T) t);
			} else {
				List<T> listVO = new ArrayList<T>();
				listVO.add((T) t);
				keyMap.put((K) key, listVO);
			}
		}
		return keyMap;
	}

	/**
	 * ����aggVO��ȡ�ӱ�pks����
	 * 
	 * @param vos
	 * @param feildName
	 * @return
	 */
	public static <T> String[] getItmePrimaryKeys(AbstractBill[] vos) {
		return getItmePrimaryKeys(vos, null);
	}

	/**
	 * ����aggVO��ȡ�ӱ�pks����
	 * 
	 * @param vos
	 * @param feildName
	 * @return
	 */
	public static <T> String[] getItmePrimaryKeys(AbstractBill[] vos,
			Class<? extends ISuperVO> clazz) {
		Map<Object, ?> keyMpa = toFieldItemMap(vos, null, clazz);
		return isNotEmpty(keyMpa) ? keyMpa.keySet().toArray(new String[0])
				: null;
	}

	/**
	 * ��vosת����ָ���ֶ�ֵΪkey��map
	 * 
	 * @param <T>
	 * @param voList
	 * @param field
	 *            �����ֶ�
	 * 
	 * @return Map<fieldValue,List<T>>
	 */
	public static <K, T extends AbstractBill> Map<K, List<T>> toKeyItemMap(
			T[] vos, String[] fields) {
		return toKeyItemMap(vos, fields, null);
	}

	/**
	 * ��vosת����ָ���ֶ�ֵΪkey��map
	 * 
	 * @param <T>
	 * @param voList
	 * @param field
	 *            �����ֶ�
	 * 
	 * @return Map<fieldValue,List<T>>
	 */
	public static <K, T extends AbstractBill> Map<K, List<T>> toKeyItemMap(
			T[] vos, String[] fields, Class<? extends ISuperVO> clazz) {
		if (isEmpty(vos) || isEmpty(fields)) {
			return null;
		}
		Map<K, List<T>> keyMap = new LinkedHashMap<K, List<T>>();
		for (AbstractBill t : vos) {
			if (isEmpty(t)) {
				continue;
			}
			ISuperVO[] bvos = null;
			if (isNotEmpty(clazz)) {
				bvos = t.getChildren(clazz);
			} else {
				bvos = (ISuperVO[]) t.getChildrenVO();
			}
			if (isEmpty(bvos)) {
				continue;
			}
			for (ISuperVO bvo : bvos) {
				if (isEmpty(bvo)) {
					continue;
				}
				StringBuffer buffer = new StringBuffer("");
				for (String field : fields) {
					Object value = bvo.getAttributeValue(field);
					buffer.append(isNotEmpty(value) ? value : "");
				}
				if (isEmpty(buffer)) {
					continue;
				}
				String key = buffer.toString().trim();
				if (keyMap.containsKey(key)) {
					keyMap.get(key).add((T) t);
				} else {
					List<T> listVO = new ArrayList<T>();
					listVO.add((T) t);
					keyMap.put((K) key, listVO);
				}
			}
		}
		return keyMap;
	}

	/**
	 * ��aggvosת���ɱ���ָ���ֶ�ֵ�ļ���
	 * 
	 * @param @param vos
	 * @param @param field
	 * @param @param distinct
	 * @param @return
	 * @return String[]
	 * @throws
	 */
	public static <T extends AbstractBill> String[] getItemStringValues(
			T[] vos, String field, boolean distinct) {
		return getItemStringValues(vos, field, null, distinct);
	}

	/**
	 * ��aggvosת���ɱ���ָ���ֶ�ֵ�ļ���
	 * 
	 * @param @param vos
	 * @param @param field
	 * @param @param distinct
	 * @param @return
	 * @return String[]
	 * @throws
	 */
	public static <T extends AbstractBill> String[] getItemStringValues(
			T[] vos, String field, Class<? extends ISuperVO> clazz,
			boolean distinct) {
		Object[] objs = getItemObjectValues(vos, field, clazz, distinct);
		return convertArrayType(objs);
	}

	/**
	 * ��aggvosת���ɱ���ָ���ֶ�ֵ�ļ���
	 * 
	 * @param <T>
	 * @param voList
	 * @param field
	 *            �����ֶ�
	 * 
	 * @return Map<fieldValue,List<T>>
	 */
	public static <T extends AbstractBill> Object[] getItemObjectValues(
			T[] vos, String field, boolean distinct) {
		return getItemObjectValues(vos, field, null, distinct);
	}

	/**
	 * ��aggvosת���ɱ���ָ���ֶ�ֵ�ļ���
	 * 
	 * @param @param vos
	 * @param @param field
	 * @param @param clazz
	 * @param @param distinct
	 * @param @return
	 * @return Object[]
	 */
	public static <T extends AbstractBill> Object[] getItemObjectValues(
			T[] vos, String field, Class<? extends ISuperVO> clazz,
			boolean distinct) {
		if (isEmpty(vos)) {
			return null;
		}
		List<Object> objs = new ArrayList<Object>();
		for (AbstractBill t : vos) {
			if (isEmpty(t)) {
				continue;
			}
			ISuperVO[] bvos = null;
			if (isNotEmpty(clazz)) {
				bvos = t.getChildren(clazz);
			} else {
				bvos = (ISuperVO[]) t.getChildrenVO();
			}
			if (isEmpty(bvos)) {
				continue;
			}
			for (ISuperVO bvo : bvos) {
				if (isEmpty(bvo)) {
					continue;
				}
				Object value = null;
				if (isNotEmpty(field)) {
					value = bvo.getAttributeValue(field);
				} else {
					value = bvo.getPrimaryKey();
				}
				if (isNotEmpty(value)) {
					if (distinct) {
						if (objs.contains(value)) {
							continue;
						}
						objs.add(value);
					} else {
						objs.add(value);
					}
				}
			}
		}
		return isNotEmpty(objs) ? objs.toArray() : null;
	}

	/**
	 * ����key�����򡢽�������
	 * 
	 * @param @param asc
	 * @param @param map
	 * @param @return
	 * @return Map<K,V>
	 * @throws
	 */
	public static <K, V> Map<K, V> keyComparator(final boolean asc,
			Map<K, V> map) {
		Map<K, V> treeMap = new TreeMap<K, V>(new Comparator<K>() {
			@Override
			public int compare(K obj1, K obj2) {
				String a = obj1.toString();
				String b = obj2.toString();
				if (obj1 instanceof String) {
					if (NumberUtils.isNumber(a) && NumberUtils.isNumber(b)) {
						if (asc) {
							return new BigDecimal(a)
									.compareTo(new BigDecimal(b));
						}
						return new BigDecimal(b).compareTo(new BigDecimal(a));
					} else {
						if (asc) {
							return a.compareTo(b);
						}
						return b.compareTo(a);
					}
				} else {
					if (asc) {
						return CompareToBuilder.reflectionCompare(obj1, obj2);
					}
					return CompareToBuilder.reflectionCompare(obj2, obj1);
				}
			}
		});
		treeMap.putAll(map);
		return treeMap;
	}

	/**
	 * �������ݽӿڣ����ڼ��Ϸ���ʱ����ȡ�����ֶε�����
	 */
	public interface GroupBy<T> {
		T groupby(Object obj);
	}

	/**
	 * @Title: group
	 * @Description:
	 * @param @param colls ���ϳ������
	 * @param @param gb �ӿڶ���
	 * @param @return ����
	 * @return Map<T,List<D>> ��������
	 * @throws
	 */
	public static final <K, V> Map<K, List<V>> group(Collection<V> colls,
			GroupBy<K> gb) {
		if (isEmpty(colls)) {
			return null;
		}
		if (isEmpty(gb)) {
			return null;
		}
		Iterator<V> iter = colls.iterator();
		Map<K, List<V>> map = new HashMap<K, List<V>>();
		while (iter.hasNext()) {
			V d = iter.next();
			K t = gb.groupby(d);
			if (map.containsKey(t)) {
				map.get(t).add(d);
			} else {
				List<V> list = new ArrayList<V>();
				list.add(d);
				map.put(t, list);
			}
		}
		return map;
	}

}
