package Acceptor;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//此类用来测试
public class UserAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    private String username;
    private String passwd;
    private File mPhoto;
    private String mPhotoFileName;
    private String mPhotoContentType;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPasswd() {
        return passwd;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public String login() throws IOException {
        System.out.println(" ---:"+username+" "+passwd);
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer = response.getWriter();
        writer.write("login success!");
        writer.flush();
        return null;
    }

    public String postFile() throws IOException{
        HttpServletRequest request = ServletActionContext.getRequest();
        ServletInputStream is = request.getInputStream();

        String dir=ServletActionContext.getServletContext().getRealPath("files");
        File file=new File(dir,"test.jpg");
        FileOutputStream fos = new FileOutputStream(file);
        int len=0;
        byte[]buf=new byte[1024];
        while((len=is.read(buf))!=-1){
            fos.write(buf,0,len);
        }

        fos.flush();
        fos.close();
//        for(int i=1;i<1000;i++){
//            File file=new File(dir,"test"+i+".jpg");
//            FileOutputStream fos=new FileOutputStream(file);
//            int len=0;
//            byte[]buf=new byte[1024];
//            while((len=is.read(buf))!=-1){
//                fos.write(buf,0,len);
//            }
//            fos.flush();
//            fos.close();
//        }

        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer = response.getWriter();
        writer.write("post success!");
        writer.flush();
        return null;
    }

    public String postString() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        ServletInputStream inputStream = request.getInputStream();

        StringBuilder stringBuilder = new StringBuilder();
        int len = 0;
        byte[] buf = new byte[1024];

        while((len = inputStream.read(buf)) != -1){
            String string = new String(buf, 0, len);
            stringBuilder.append(string);
        }
//        System.out.println(stringBuilder.toString());
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer = response.getWriter();
        writer.write(stringBuilder.toString());
        writer.flush();
        return null;
    }
}
