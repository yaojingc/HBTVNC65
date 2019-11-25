package nc.ui.erm.billpub.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.er.exp.cmd.ServiceFeeCalcVO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 * ����������˰���㹤����
 * 
 * ���񱨳����ã�ÿ�����벻������ǧԪ�ģ��������ð˰�Ԫ����ǧԪ���ϵģ������ٷ�֮��ʮ�ķ��ã�����
 * ��ΪӦ��˰���öȻ�����ñ���˰�ʣ�˰��Ϊ�ٷ�֮��ʮ��
 * 
 *
 * |����(x)         |Ӧ��˰����(y)   |      ����    		  |  ˰��               | ��˰��                             | �ٿ�ϵ��               |
 * -----------------------------------------------------------------------------------------------
 * |x<=800         |      0       |     0             |  0        | 0               | 0           |
 * |800<x<=4000    |    x-800     |    y<=20000       |  20%      | (x-800)*20%     | 0           |
 * |4000<x<=20000  |    0.8*x     |    y<=20000       |  20%      | 0.8x*20%        | 0           |
 * |20000<x<=50000 |    0.8*x     |    20000<y<=50000 |  30%      | 0.8x*30%        | 2000        |
 * |x>50000        |    0.8*x     |    y>50000        |  40%      | 0.8x*40%        | 7000        |
 *
 *
 *  Ӧ��˰���ö� = ������ã�������4000Ԫ�� - 800Ԫ
	Ӧ��˰���ö� = ������ã�����4000Ԫ������1 - 20%��
	Ӧ��˰�� = Ӧ��˰���ö� ��14%
 *
 *
 * @author yao
 */
public class ServiceFeeTaxUtils {
	
	/**
     * ����һ����������С�����ȷλ��λ����С�������λ����2λ���������룬���ڿ��Ը�������
     */
    private static final int DIGIT_NUMBER_AFTER_DOT = 2;
	
    // ����һ�������������ѱ������Ľ�������
 	public static final String TRANSTYPE = "264X-Cxx-01";
	
    /**
     * ���������˰ǰ����ѣ�����õ�˰�������
     * @param originServiceFee
     * @return
     */
	public static Double serviceFeeCaculateAfterTaxByPreTax(double originServiceFee){
		//Ӧ��˰����
        double taxIncome = 0;
        double taxRate = 0;
        double quickDeducData = 0;
        if (originServiceFee <= 800) {
            return originServiceFee;
        } else if (originServiceFee > 800 && originServiceFee <= 4000) {
            taxIncome = originServiceFee - 800;
            taxRate = 0.2;
        }
        if (originServiceFee > 4000) {
            taxIncome = originServiceFee * 0.8;
        }
        if (taxIncome <= 20000) {
            taxRate = 0.2;
        } else if (taxIncome > 20000 && taxIncome <= 50000) {
            taxRate = 0.3;
            quickDeducData = 2000;
        } if (taxIncome > 50000) {
            taxRate = 0.4;
            quickDeducData = 7000;
        }
        double incomeAfterTax = originServiceFee - (taxIncome * taxRate - quickDeducData);
        return roundHalfUpValue(incomeAfterTax, DIGIT_NUMBER_AFTER_DOT);
	}
	
	
	/**
	 * ���������˰������ѣ�����õ�˰ǰ�����
	 * @param afterTaxServiceFee
	 * @return
	 */
	public static Double serviceFeeCaculatePreTaxByAfterTax(double afterTaxServiceFee){
		//Ӧ��˰����
        double taxIncome = 0;
        double tax = 0;
        
        if (afterTaxServiceFee <= 800) {
            return afterTaxServiceFee;
        } else if (afterTaxServiceFee > 800 && afterTaxServiceFee <= 3360) {
        	// Ӧ��˰���ö�=������˰�����-800���£�1-˰�ʣ�
            taxIncome = (afterTaxServiceFee - 800) / (1-0.2) + 800;
        } else if (afterTaxServiceFee > 3360 && afterTaxServiceFee <= 21000) {
        	// Ӧ��˰���ö[������˰��������۳���������1��20%��]��[1��˰�ʡ���1��20%��]
        	taxIncome = (afterTaxServiceFee * 0.8) / (1-0.2*(1-0.2)) ;
        	// Ӧ��˰��=Ӧ��˰���ö������˰��-����۳��� 
        	tax = taxIncome * 0.2;
        	taxIncome = afterTaxServiceFee + tax;
        } else if (afterTaxServiceFee > 21000 && afterTaxServiceFee <= 49500) {
        	taxIncome = ((afterTaxServiceFee - 2000) * 0.8) / 0.76 ;
        	// Ӧ��˰��=Ӧ��˰���ö������˰��-����۳��� 
        	tax = taxIncome * 0.3 - 2000;
        	taxIncome = afterTaxServiceFee + tax;
        } else if (afterTaxServiceFee > 49500) {
        	taxIncome = ((afterTaxServiceFee - 7000) * 0.8) / 0.68 ;
        	// Ӧ��˰��=Ӧ��˰���ö������˰��-����۳��� 
        	tax = taxIncome * 0.4 - 7000;
        	taxIncome = afterTaxServiceFee + tax;
        }
        return roundHalfUpValue(taxIncome, DIGIT_NUMBER_AFTER_DOT);
	}
	
	
	
	/**
	 * ����˰ǰ�ĸ�Ѽ���˰��ĸ��
	 * @param gaoFee
	 * @return
	 */
	public static Double airticleFeeCaculateAfterTaxByPreTax(double airticleFee){
		// Ӧ��˰����
		double taxIncome = 0;
        
        if(airticleFee <= 800){
        	return airticleFee;
        }else if (airticleFee > 800 && airticleFee <= 4000) {
            taxIncome = airticleFee - 800;
        }
        
        if (airticleFee > 4000) {
            taxIncome = airticleFee * 0.8;
        }
        
        // Ӧ��˰�� = Ӧ��˰���ö� ��14%
        double incomeAfterTax = airticleFee - taxIncome * 0.14;
		
		return roundHalfUpValue(incomeAfterTax,DIGIT_NUMBER_AFTER_DOT);
	}
	
	
	/**
	 * ����˰��ĸ�Ѽ���˰ǰ�ĸ��
	 * @param gaoFee
	 * @return
	 */
	public static Double airticleFeeCaculatePreTaxByAfterTax(double airticleFee){
		// Ӧ��˰����
		double taxIncome = 0;
        
        if(airticleFee <= 800){
        	return airticleFee;
        } else if (airticleFee > 800 && airticleFee <= 3552) {
        	
        	taxIncome = ( airticleFee - 800 ) / 0.86 +800;
        	
        } else if (airticleFee > 3552) {
        	
        	taxIncome = airticleFee / 0.888;
        	
        }
        
		return roundHalfUpValue(taxIncome,DIGIT_NUMBER_AFTER_DOT);
				 
	}
	
	
	
	/**
	 * ���ݾ��ȴ�����
	 * @param value
	 * @param digitNumberAfterDot
	 * @return
	 */
    private static double roundHalfUpValue(double value, int digitNumberAfterDot) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(digitNumberAfterDot, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    
    
    /**
     * ͨ�����֤�ţ����µ����ڲ�ѯ���˵���ʷ����ѱ�������������õ�������Ҫ�� �ۼ�Ӧ�� �� ������˰��ֵ
     * @param idcardno ���֤��
     * @param currentdate ���µ�����  yyyy-mm
     * @return  Map<���֤��,����ۼ�Ӧ����������˰��VO> 
     */
    public static Map<String,ServiceFeeCalcVO>  getFeeMapByIDCardNO(String idcardno,String currentdate){
    	SqlBuilder sql = new SqlBuilder();
    	sql.append(" SELECT ");
    	sql.append(" 	tb_h.approver  as approver, ");// ��ͷ��������
    	sql.append(" 	tb_b.defitem42 as idcardno, ");// ���֤��
    	sql.append(" 	tb_b.amount    as yfje, ");// Ӧ�����
    	sql.append(" 	tb_b.defitem47 as ljyf, ");// �ۼ�Ӧ��
    	sql.append(" 	tb_b.defitem46 as bcyjks, ");// ����Ԥ�ƿ�˰
    	sql.append(" 	tb_b.defitem45 as zbks, ");// ������˰
    	sql.append(" 	tb_b.defitem44 as sfje, ");// ʵ�����
    	sql.append(" 	tb_b.ts	   	   as bodydate ");// ���������
    	sql.append(" FROM ");
    	sql.append(" 	er_busitem tb_b ");
    	sql.append(" LEFT JOIN ");
    	sql.append(" 	er_bxzb tb_h ");
    	sql.append(" ON ");
    	sql.append(" 	tb_h.pk_jkbx = tb_b.pk_jkbx ");
    	sql.append(" WHERE ");
    	sql.append(" 	tb_h.djlxbm ", TRANSTYPE);
    	sql.append(" AND ");
    	sql.append(" 	tb_b.defitem42 ",idcardno);
    	sql.append(" AND ");
    	sql.append(" 	tb_b.ts like '"+currentdate+"%'");
    	sql.append(" AND ");
    	sql.append(" 	tb_h.Approver ");
    	sql.append(" IN (");
    	sql.append(" 		SELECT ");
    	sql.append(" 			wfgroup_b.pk_member ");
    	sql.append(" 		FROM ");
    	sql.append(" 			pub_wfgroup_b wfgroup_b ");
    	sql.append(" 		LEFT JOIN ");
    	sql.append(" 			pub_wfgroup wfgroup ");
    	sql.append(" 		ON ");
    	sql.append(" 			wfgroup.pk_wfgroup = wfgroup_b.pk_wfgroup ");
    	sql.append(" 		WHERE");
    	sql.append(" 			wfgroup.code = 'kj' ");
    	sql.append(" 		AND ");
    	sql.append(" 			wfgroup.name = '���' ");
    	sql.append(" 	)");
    	
    	
    	IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
    	
    	// ��ʼ����ʷ�ۼ�Ӧ�����
    	UFDouble ljyf = new UFDouble();
    	// ��ʼ�����յ���ʷ����Ԥ�ƿ�˰��� �� ��ʷ������˰���ĺ�
    	UFDouble taxTotal = new UFDouble();
    	
    	try {
    		// �õ�������������ʷ����ѱ���������
			List<ServiceFeeCalcVO> list = (List<ServiceFeeCalcVO>) queryBS
					.executeQuery(sql.toString(), new BeanListProcessor(ServiceFeeCalcVO.class));    		
    		
    		if(HRATCommonUtils.isNotEmpty(list)){
    			for (int i = 0; i < list.size(); i++) {
					ServiceFeeCalcVO serviceFeeCalcVO  = (ServiceFeeCalcVO)list.get(i);
					
					UFDouble currLjyf = serviceFeeCalcVO.getLjyf() == null?new UFDouble(0):serviceFeeCalcVO.getLjyf();
					// �ۼ���ʷ���ۼ�Ӧ��
					ljyf = ljyf.add(currLjyf);
					
					UFDouble currBcyjks = serviceFeeCalcVO.getBcyjks() == null?new UFDouble(0):serviceFeeCalcVO.getBcyjks();
					UFDouble currZbks = serviceFeeCalcVO.getZbks() == null?new UFDouble(0):serviceFeeCalcVO.getZbks();
					// �ۼ���ʷ����Ԥ�ƿ�˰��� �� ��ʷ������˰���
					taxTotal = taxTotal.add(currBcyjks).add(currZbks);
    			}
    			
    			ServiceFeeCalcVO resultVO = new ServiceFeeCalcVO();
    			resultVO.setIdcardno(idcardno);
    			
    			if(ljyf.isTrimZero()){
    				resultVO.setLjyf(new UFDouble(0));
    			}else{
    				resultVO.setLjyf(ljyf);
    			}
    			
    			// ��������ʷ�� ����Ԥ�ƿ�˰�Լ�������˰ȫ���ۼӵ� һ��ȫ��VO��������˰�ֶ���
    			resultVO.setZbks(taxTotal);
    			
    			Map<String,ServiceFeeCalcVO> map = new HashMap<String,ServiceFeeCalcVO>();
    			map.put(idcardno, resultVO);
    			
    			return map;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
	
	
}
