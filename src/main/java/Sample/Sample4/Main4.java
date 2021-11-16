package Sample.Sample4;

import cn.textcheck.CheckManager;
import cn.textcheck.engine.pojo.LocalPaperLibrary;
import cn.textcheck.engine.pojo.Paper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 横向查重/标书查重 实现实例
 *
 * 此应用程序使用了北京芯锋科技有限公司的XINCHECK SDK许可软件，该许可软件版权归北京芯锋科技有限公司所有，且其所有权利由北京芯锋科技有限公司保留。许可证密钥的使用应遵守XINCHECK软件许可使用协议，否则将违反中华人民共和国和国际版权法以及其他适用法律。
 */
public class Main4 {

    public static void main(String[] args) throws Exception {
        //设置授权许可证（免费获取评估许可证：https://xincheck.com/?id=7）
        CheckManager.INSTANCE.setRegCode("muQyymFW0ysAZZhKVOzkh/jbuGMMfBg9IihiT2Fq9xEZxfIA=");

        //待查重文件所在的文件夹路径
        String path = "待查重文件所在的<文件夹>路径";

        //通过<文件夹>加载比对库。由于是横向查重，因此待查重的文件同时也作为比对库
        LocalPaperLibrary paperLibrary = LocalPaperLibrary.load(new File(path));

        //通过<文件夹>批量加载待查重的文件
        File[] files = new File(path).listFiles();
        List<Paper> papers = new ArrayList<>();
        for (File file : files) {
            Paper paper = Paper.load(file);
            paper.setPayload(file.getName()); //将文件名存入待查paper的payload，保存查重报告时将使用。
            papers.add(paper);
        }

        //对于标书场景，有一些内容是允许重复的，如招标文件、技术规格说明书中的内容。可以通过添加白名单的方式，在查重时进行排除
        List<String> whiteList = new ArrayList<>();
        whiteList.add(Paper.load(new File("技术规格说明书的文件路径")).getText());
        whiteList.add(Paper.load(new File("招标文件的文件路径")).getText());
        whiteList.add("XXXXX允许重复的字符串内容");

        //对于标书场景，有一些内容是无法查重但需要重点关注的。如施工城市、供应商公司等。可以通过添加重点关注清单，将这些内容在查重报告中高亮展示
        List<String> blackList = new ArrayList<>();
        //重点关注清单中的字符串长度不能超过8个字符，否则自动阶段为8
        blackList.add("XXX有限公司");
        blackList.add("XXX城市");

        //实例化一个用于保存上下文的实例
        Context context = new Context();
        context.reportPath = "查重报告保存的<文件夹>路径（例D:\\Report）";

        //构建并启动任务
        CheckManager.INSTANCE
                .getCheckTaskBuilder() //获取查重任务构造器
                .setUid("1") //设置任务id
                .addCheckState(new CheckStateImp(), context) //添加回调处理并传递上下文
                .addLibrary(paperLibrary) //添加比对库。假设本次查重只需要用到1和3两个比对库
                .addCheckPaper(papers) //添加待查Paper
                .addWhiteWord(whiteList)
                .addBlackWord(blackList)
                .build() //构建任务
                .submit(); //启动任务。submit：将任务提交到线程池中，如果线程池繁忙将会排队。start：直接启动任务

    }

}
