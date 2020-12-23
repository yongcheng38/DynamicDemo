import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resources;
import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


public class Demo {
    public static void main(String[] args) {
        try {
//            InputStream inputStream = Resources.class.getResourceAsStream("通用二维栅格面数据.ngz");
            File file = new File("C:\\Users\\wang\\Desktop\\结果样例数据\\通用二维栅格面数据.ngz");
            FileInputStream fis = new FileInputStream(file);
            byte[] byte1 = new byte[1];
            byte[] byte4 = new byte[4];
            byte[] byte8 = new byte[8];
            int len,i;
            double d;
            BigDecimal d1;
            char c;
            fis.read(byte4);
            i = ByteBuffer.wrap(byte4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            System.out.println("结果类别:"+i);
            fis.read(byte4);
            i = ByteBuffer.wrap(byte4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            System.out.println("结果单位:"+i);
            fis.read(byte4);
            i = ByteBuffer.wrap(byte4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            System.out.println("结果数据类别（T）:"+i);
            fis.read(byte4);
            i = ByteBuffer.wrap(byte4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            System.out.println("坐标系统:"+i);

            fis.read(byte8);
            d = ByteBuffer.wrap(byte8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
            System.out.println("左边界:"+d);
            fis.read(byte8);
            d = ByteBuffer.wrap(byte8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
            System.out.println("下边界:"+d);
            fis.read(byte8);
            d = ByteBuffer.wrap(byte8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
            d1= BigDecimal.valueOf(d);
            System.out.println("横向分辨率:"+d1);
            fis.read(byte8);
            d = ByteBuffer.wrap(byte8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
            d1= BigDecimal.valueOf(d);
            System.out.println("纵向分辨率:"+d1);

            fis.read(byte4);
            i = ByteBuffer.wrap(byte4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            System.out.println("横向栅格数量(w):"+i);

            fis.read(byte4);
            i = ByteBuffer.wrap(byte4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            System.out.println("纵向栅格数（h）:"+i);

            fis.read(byte1);
            System.out.println("数据存放标记:"+ byte1[0]);
//            c = ByteBuffer.wrap(byte1).order(ByteOrder.LITTLE_ENDIAN).getChar();
//            System.out.println("数据存放标记:"+c);

            List<Double> list = new ArrayList<Double>();
            while((len=fis.read(byte8))!=-1){
                d = ByteBuffer.wrap(byte8).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                list.add(d);
            }
            System.out.println(list);
//            fis.read(byte8);
//            d = ByteBuffer.wrap(byte8).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//            System.out.println("xxxxx:"+d);
//
//            fis.read(byte8);
//            d = ByteBuffer.wrap(byte8).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//            System.out.println("xxxxx:"+d);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
