package Sample.Sample2;

import cn.textcheck.CheckState;
import cn.textcheck.engine.pojo.Paper;
import cn.textcheck.engine.report.Reporter;
import cn.textcheck.engine.type.ReportType;

import java.io.IOException;
import java.util.List;

/**
 * SDK进阶使用范例：CheckState实现范例
 */
public class CheckStateImp implements CheckState<String> {

    public void taskStart(String uid, List<Paper> toCheckPapers, String context) {
        System.out.println("task start:" + uid);
    }

    public void taskFinish(String uid, List<Paper> toCheckPapers, List<Reporter> reporters, List<Paper> failedPapers, String context) {
        System.out.println("task finish:" + uid);
    }

    public void paperStart(String uid, Paper paper, String context) {
        System.out.println("paper start:" + uid);
    }

    public void paperSuccess(String uid, Reporter reporter, String context) {
        System.out.println("paper success:" + uid);
        System.out.println(reporter.getReportId()); //打印查重报告id
        System.out.println(reporter.getCopyRate()); //打印总重复率
        System.out.println(reporter.getFrontCopyWords()); //打印前部重复字符数
        try {
            //保存两种类型的查重报告
            reporter.saveAsFile("保存html查重报告的文件路径（例 C:\\Desktop\\report1.html）", ReportType.TEXT_WITH_CITATION); //保存全文标明引文查重报告
            reporter.saveAsFile("保存html查重报告的文件路径（例 C:\\Desktop\\report2.html）", ReportType.TEXT_WITH_ORIGINAL); //保存原文对照查重报告
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paperFailed(String uid, Paper paper, int code, Throwable t, String context) {
        System.out.println("paper failed:" + uid);
        t.printStackTrace();
    }
}
