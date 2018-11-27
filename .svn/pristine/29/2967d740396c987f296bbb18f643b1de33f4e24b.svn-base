package cn.com.cdboost.charge.webapi.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wt
 * @desc
 * @create in  2018/7/10
 **/
public class DownLoadUtil {
    public static void downExcel(HttpServletResponse response, XSSFWorkbook workBook) throws IOException {
        response.reset();
        ServletOutputStream out;
        // 设置response流信息的头类型，MIME码
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + new String(("导出表" + System.currentTimeMillis() + ".xls").getBytes("utf-8"), "ISO8859_1")
                + "\"");
        // 创建输出流对象
        out = response.getOutputStream();
        // 将创建的Excel对象利用二进制流的形式强制输出到客户端去
        workBook.write(out);
        response.addHeader("Content-Length",out.toString());
        // 强制将数据从内存中保存
        out.flush();
        out.close();
    }
}
