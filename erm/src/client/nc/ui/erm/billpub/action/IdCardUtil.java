package nc.ui.erm.billpub.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nc.util.mmf.framework.base.MMValueCheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ���֤�Ϸ���У�鹤����
 * 
 * --15λ���֤���룺��7��8λΪ�������(��λ��)����9��10λΪ�����·ݣ���11��12λ����������ڣ���15λ�����Ա�����Ϊ�У�ż��ΪŮ��
 * --18λ���֤����
 * ����7��8��9��10λΪ�������(��λ��)����11����12λΪ�����·ݣ���13��14λ����������ڣ���17λ�����Ա�����Ϊ�У�ż��ΪŮ��
 * ���һλΪУ��λ
 *  ���� 11 ��� 12 �ӱ� 13 ɽ�� 14 ���� 15 ���� 21 ���� 22 �ڡ� 23 �Ϻ� 31 ���� 32 �㽭 33 ���� 34
	���� 35  ���� 36   ɽ�� 37   ���� 41  ���� 42   ���� 43  �㶫 44  ���� 45   ���� 46   �Ĵ� 51  ���� 51   ���� 52  ���� 53 ���� 54 ���� 61 ���� 62 �ຣ 63 ���� 64 �½� 65
	
 * @author yaojing
 */
public class IdCardUtil {

	/**
	 * ʡ��ֱϽ�д���� 11 : ���� 12 : ��� 13 : �ӱ� 14 : ɽ�� 15 : ���ɹ� 21 : ���� 22 : ���� 23 :
	 * ������ 31 : �Ϻ� 32 : ���� 33 : �㽭 34 : ���� 35 : ���� 36 : ���� 37 : ɽ�� 41 : ���� 42 :
	 * ���� 43 : ���� 44 : �㶫 45 : ���� 46 : ���� 50 : ���� 51 : �Ĵ� 52 : ���� 53 : ���� 54 :
	 * ���� 61 : ���� 62 : ���� 63 : �ຣ 64 : ���� 65 : �½� 71 : ̨�� 81 : ��� 82 : ���� 91 :
	 * ����
	 */
	private static String[] cityCode = { "11", "12", "13", "14", "15", "21",
			"22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42",
			"43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
			"63", "64", "65", "71", "81", "82", "91" };

	/**
	 * ÿλ��Ȩ����
	 */
	private static int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
			8, 4, 2 };

	/**
	 * ��֤���е����֤�ĺϷ���
	 * 
	 * @param idcard
	 *            ���֤
	 * @return �Ϸ�����true�����򷵻�false
	 */
	public static boolean isValidatedAllIdcard(String idcard) {
		if (idcard == null || "".equals(idcard)) {
			return false;
		}
		int s = 15;
		if (idcard.length() == s) {
			return validate15IDCard(idcard);
		}
		int s1 = 18;
		if (idcard.length() == s1) {
			return validate18Idcard(idcard);
		}
		return false;

	}

	/**
	 * �ж�18λ���֤�ĺϷ���
	 * 
	 * ���ݡ��л����񹲺͹����ұ�׼GB11643-1999�����йع�����ݺ���Ĺ涨��������ݺ�������������룬��ʮ��λ���ֱ������һλ����У������ɡ�
	 * ����˳�������������Ϊ����λ���ֵ�ַ�룬��λ���ֳ��������룬��λ����˳�����һλ����У���롣
	 * 
	 * ˳����: ��ʾ��ͬһ��ַ������ʶ������Χ�ڣ���ͬ�ꡢͬ�¡�ͬ �ճ������˱ඨ��˳��ţ�˳�����������������ԣ�ż������ ��Ů�ԡ�
	 * 
	 * 1.ǰ1��2λ���ֱ�ʾ������ʡ�ݵĴ��룻 2.��3��4λ���ֱ�ʾ�����ڳ��еĴ��룻 3.��5��6λ���ֱ�ʾ���������صĴ��룻
	 * 4.��7~14λ���ֱ�ʾ�������ꡢ�¡��գ� 5.��15��16λ���ֱ�ʾ�����ڵص��ɳ����Ĵ��룻
	 * 6.��17λ���ֱ�ʾ�Ա�������ʾ���ԣ�ż����ʾŮ�ԣ�
	 * 7.��18λ������У���룺Ҳ�е�˵�Ǹ�����Ϣ�룬һ��������������������������������֤����ȷ�ԡ�У���������0~9�����֣���ʱҲ��x��ʾ��
	 * 
	 * ��ʮ��λ����(У����)�ļ��㷽��Ϊ�� 1.��ǰ������֤����17λ���ֱ���Բ�ͬ��ϵ�����ӵ�һλ����ʮ��λ��ϵ���ֱ�Ϊ��7 9 10 5 8 4
	 * 2 1 6 3 7 9 10 5 8 4 2
	 * 
	 * 2.����17λ���ֺ�ϵ����˵Ľ����ӡ�
	 * 
	 * 3.�üӳ����ͳ���11���������Ƕ���
	 * 
	 * 4.����ֻ������0 1 2 3 4 5 6 7 8 9 10��11�����֡���ֱ��Ӧ�����һλ���֤�ĺ���Ϊ1 0 X 9 8 7 6 5 4 3
	 * 2��
	 * 
	 * 5.ͨ�������֪���������2���ͻ������֤�ĵ�18λ�����ϳ����������ֵĢ������������10�����֤�����һλ�������2��
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean validate18Idcard(String idcard) {
		if (idcard == null) {
			return false;
		}

		// ��18λΪ��
		int s = 18;
		if (idcard.length() != s) {
			// logger.error("���֤λ������ȷ!");
			return false;
		}
		// ��ȡǰ17λ
		String idcard17 = idcard.substring(0, 17);

		// ǰ17λȫ��Ϊ����
		if (!isDigital(idcard17)) {
			return false;
		}

		String provinceid = idcard.substring(0, 2);
		// У��ʡ��
		if (!checkProvinceid(provinceid)) {
			return false;
		}

		// У���������
		String birthday = idcard.substring(6, 14);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {
			Date birthDate = sdf.parse(birthday);
			String tmpDate = sdf.format(birthDate);
			// ���������ղ���ȷ
			if (!tmpDate.equals(birthday)) {
				return false;
			}

		} catch (ParseException e1) {

			return false;
		}

		// ��ȡ��18λ
		String idcard18Code = idcard.substring(17, 18);

		char c[] = idcard17.toCharArray();

		int bit[] = converCharToInt(c);

		int sum17 = 0;

		sum17 = getPowerSum(bit);

		// ����ֵ��11ȡģ�õ���������У�����ж�
		String checkCode = getCheckCodeBySum(sum17);
		if (null == checkCode) {
			return false;
		}
		// �����֤�ĵ�18λ���������У�����ƥ�䣬����Ⱦ�Ϊ��
		if (!idcard18Code.equalsIgnoreCase(checkCode)) {
			return false;
		}
		// System.out.println("��ȷ");
		return true;
	}

	/**
	 * У��15λ���֤
	 * 
	 * <pre>
	 * ֻУ��ʡ�ݺͳ���������
	 * </pre>
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean validate15IDCard(String idcard) {
		if (idcard == null) {
			return false;
		}
		// ��15λΪ��
		int s = 15;
		if (idcard.length() != s) {
			return false;
		}

		// 15ȫ��Ϊ����
		if (!isDigital(idcard)) {
			return false;
		}

		String provinceid = idcard.substring(0, 2);
		// У��ʡ��
		if (!checkProvinceid(provinceid)) {
			return false;
		}

		String birthday = idcard.substring(6, 12);

		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

		try {
			Date birthDate = sdf.parse(birthday);
			String tmpDate = sdf.format(birthDate);
			// ���֤���ڴ���
			if (!tmpDate.equals(birthday)) {
				return false;
			}

		} catch (ParseException e1) {

			return false;
		}

		return true;
	}

	/**
	 * ��15λ�����֤ת��18λ���֤
	 * 
	 * @param idcard
	 * @return
	 */
	public static String convertIdcarBy15bit(String idcard) {
		if (idcard == null) {
			return null;
		}

		// ��15λ���֤
		int s = 15;
		if (idcard.length() != s) {
			return null;
		}

		// 15ȫ��Ϊ����
		if (!isDigital(idcard)) {
			return null;
		}

		String provinceid = idcard.substring(0, 2);
		// У��ʡ��
		if (!checkProvinceid(provinceid)) {
			return null;
		}

		String birthday = idcard.substring(6, 12);

		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

		Date birthdate = null;
		try {
			birthdate = sdf.parse(birthday);
			String tmpDate = sdf.format(birthdate);
			// ���֤���ڴ���
			if (!tmpDate.equals(birthday)) {
				return null;
			}

		} catch (ParseException e1) {
			return null;
		}

		Calendar cday = Calendar.getInstance();
		cday.setTime(birthdate);
		String year = String.valueOf(cday.get(Calendar.YEAR));

		String idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);

		char c[] = idcard17.toCharArray();
		String checkCode = "";

		// ���ַ�����תΪ��������
		int bit[] = converCharToInt(c);

		int sum17 = 0;
		sum17 = getPowerSum(bit);

		// ��ȡ��ֵ��11ȡģ�õ���������У����
		checkCode = getCheckCodeBySum(sum17);

		// ��ȡ����У��λ
		if (null == checkCode) {
			return null;
		}
		// ��ǰ17λ���18λУ����ƴ��
		idcard17 += checkCode;
		return idcard17;
	}

	/**
	 * У��ʡ��
	 * 
	 * @param provinceid
	 * @return �Ϸ�����TRUE�����򷵻�FALSE
	 */
	private static boolean checkProvinceid(String provinceid) {
		for (String id : cityCode) {
			if (id.equals(provinceid)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ������֤
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isDigital(String str) {
		return str.matches("^[0-9]*$");
	}

	/**
	 * �����֤��ÿλ�Ͷ�Ӧλ�ļ�Ȩ�������֮���ٵõ���ֵ
	 * 
	 * @param bit
	 * @return
	 */
	private static int getPowerSum(int[] bit) {

		int sum = 0;

		if (power.length != bit.length) {
			return sum;
		}

		for (int i = 0; i < bit.length; i++) {
			for (int j = 0; j < power.length; j++) {
				if (i == j) {
					sum = sum + bit[i] * power[j];
				}
			}
		}
		return sum;
	}

	/**
	 * ����ֵ��11ȡģ�õ���������У�����ж�
	 * 
	 * @param checkCode
	 * @param sum17
	 * @return У��λ
	 */
	private static String getCheckCodeBySum(int sum17) {
		String checkCode = null;
		switch (sum17 % 11) {
		case 10:
			checkCode = "2";
			break;
		case 9:
			checkCode = "3";
			break;
		case 8:
			checkCode = "4";
			break;
		case 7:
			checkCode = "5";
			break;
		case 6:
			checkCode = "6";
			break;
		case 5:
			checkCode = "7";
			break;
		case 4:
			checkCode = "8";
			break;
		case 3:
			checkCode = "9";
			break;
		case 2:
			checkCode = "x";
			break;
		case 1:
			checkCode = "0";
			break;
		case 0:
			checkCode = "1";
			break;
		default:
		}
		return checkCode;
	}

	/**
	 * ���ַ�����תΪ��������
	 * 
	 * @param c
	 * @return
	 * @throws NumberFormatException
	 */
	private static int[] converCharToInt(char[] c) throws NumberFormatException {
		int[] a = new int[c.length];
		int k = 0;
		for (char temp : c) {
			a[k++] = Integer.parseInt(String.valueOf(temp));
		}
		return a;
	}

	/**
	 * @Title: getSexFromidCardNo
	 * @Description: �������֤�ŵõ��Ա�
	 * @param @param idCardNo
	 * @param @return ����
	 * @return String ��������
	 * @throws
	 */
	public static String getSexFromidCardNo(String idCardNo) {
		if(MMValueCheck.isNotEmpty(idCardNo)){
			if (Integer.parseInt(idCardNo.substring(16).substring(0, 1)) % 2 == 0) {
				return HRATCommonUtils.queryDefdocPk("Ů", "�Ա�");
			} else {
				return HRATCommonUtils.queryDefdocPk("��", "�Ա�");
			}
		}
		return null;
	}

	/**
	 * @Title: getBirthDayFromidCardNo
	 * @Description: ��7��8��9��10λΪ�������(��λ��)����11����12λΪ�����·ݣ���13��14λ�����������
	 * @param @param idCardNo
	 * @param @return ����
	 * @return String ��������
	 * @throws
	 */
	public static String getBirthDayFromidCardNo(String idCardNo) {
		if(MMValueCheck.isNotEmpty(idCardNo)){
			StringBuffer sb = new StringBuffer();
			String year = idCardNo.substring(6).substring(0, 4);// �õ����
			String month = idCardNo.substring(10).substring(0, 2);// �õ��·�
			String day = idCardNo.substring(12).substring(0, 2);// �õ���
			return sb.append(year).append("-").append(month).append("-").append(day).toString();
		}
		return null;
	}

}
