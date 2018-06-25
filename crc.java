public class CRCModbus {
    public byte[] preHandler(String param){
        /**
         * @Description: 将字符串转化为16进制数组
         * @author: Huang Hua
         * @param:  [param]
         * @return: byte[]
         * @Date: 2018/6/12
         */
        int len = param.length();

        if (len%2 != 0) return null;
        byte[] bytes = new byte[len/2];
        int times = 0;
        for(int i = 0;i < len;i = i + 2){
            String token = param.substring(i,i+2);
            byte b = (byte) Integer.parseInt(token, 16);
            bytes[times] = b;
            times++;
        }
        return bytes;
    }

    public String getCRC(byte[] bytes){
        /**
         * @Description: 生成CRC循环冗余校验码
         * @author: Huang Hua
         * @param:  [bytes]
         * @return: java.lang.String
         * @Date: 2018/6/12
         */
        int CRC = 0x0000ffff;//预置1个16位的寄存器为十六进制FFFF（即全为1）；称此寄存器为CRC寄存器；
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);//把第一个8位二进制数据 （既通讯信息帧的第一个字节）与16位的CRC寄存器的低8位相异或，把结果放于CRC寄存器；
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        String str = Integer.toHexString(CRC);
        //若CRC最前数字为0，转为int会将0忽略。
        if (str.length()==3){
            str = "0"+str;
        }else if (str.length()==2){
            str = "00"+str;
        }else if (str.length()==1){
            str = "000"+str;
        }
        return str;
    }
}
