package com.baomidou.mybatisplus.samples.crud.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: brbai
 * @create: 2019-07-09 11:29:41
 * @description:
 */
@Component
@Slf4j
public class CSVUtils {

    private static final int SOA_BANK = 1;
    private static final int SOA_ACCOUNT = 2;
    
    /** 
	 * @Description: 遍历文件夹，读取文件插入数据库
	 * @Param: [f] 目录（文件夹）
	 * @return: void 
	 */
    public void getFileName(File f) {

        //获取当前目录的子目录对象的数组
        File[] file = f.listFiles();
        //遍历当前File数组(父目录的)
        for (File file2 : file) {

            String fileName = file2.getName();
            if(!file2.isDirectory()){

                long start = System.currentTimeMillis();

                if(fileName.indexOf("soa_bank") != -1){
                    insertData(file2,SOA_BANK);
                }else if(fileName.indexOf("soa_account") != -1){
                    insertData(file2,SOA_ACCOUNT);
                }
                log.info("文件{}导入完成，用时{}ms",file2.getPath(),(System.currentTimeMillis()-start));
            }
            //如果file2是一个文件夹则递归遍历子目录，否则结束当前目录遍历
            if(file2.isDirectory()) {
                getFileName(file2);
            }
        }
    }

     /** 
      * @Description: 把数据从csv文件中读取到list
      * @Param: [reader] 字符缓冲流
      * @return: java.util.List 读取到的数据（一次读取1000行）
      */
    public List readCSV(BufferedReader reader){
        try {
            ArrayList<String> list = new ArrayList<String>();
            String line = null;
            for(int i=0; i < 1000; i++) {
                line = reader.readLine();

                if(line == null){
                    break;
                }

                line = "'"+line.replaceAll("︴","','")+"'";

                //'xxxx-xx-xx'正则表达式
                String regex1 = "['][0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[']";
                Pattern pattern1 = Pattern.compile(regex1);
                Matcher m1 = pattern1.matcher(line);
                line = line.replaceAll(regex1 ,"︴");
                //替换时间格式
                while(m1.find()){
                    line = line.replaceFirst("︴"," to_date ( "+m1.group()+", 'YYYY-MM-DD HH24:MI:SS' )");
                }

                //'xxxx-xx-xx xx:xx:xx'正则表达式
                String regex2 = "['][0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}[:][0-9]{1,2}[']";
                Pattern pattern2 = Pattern.compile(regex2);
                Matcher m2 = pattern2.matcher(line);
                line = line.replaceAll(regex2 ,"︴");
                //替换时间格式
                while(m2.find()){
                    line = line.replaceFirst("︴"," to_date ( "+m2.group()+", 'YYYY-MM-DD HH24:MI:SS' )");
                }

                list.add(line);
            }
            return list;
        } catch (Exception e) {
            log.error("文件:{}读取信息出错");
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return null;
    }

    /** 
	 * @Description: 批量插入到数据库
	 * @Param: [file, type] file：文件，type：数据库对应表
	 * @return: void 
	 */
    public void insertData(File file,int type) {


        String head = null;
        List<String> list = null;
        BufferedReader reader = null;
        //读取第一行的标题信息
        try {
            reader = new BufferedReader(new FileReader(file));
            head = reader.readLine();

            if(head == null){
                log.info("文件{}内容为空!!!",file.getPath());
                return;
            } else {
                head = head.replaceAll("︴",",");
            }

            while ((list = readCSV(reader)).size() != 0){

//                switch (type){
//                    case 1:{
//                        bankInfoMapper.insertListData(head,list);
//                    }
//                    break;
//                    case 2:{
//                        accountMapper.insertListData(head,list);
//                    }
//                    break;
//                }

                log.info("文件{}导入中--- +{}行",file.getPath(),list.size());
            }
        } catch (IOException e) {
            log.info("***文件{}导入数据库出现异常***",file.getPath());
            e.printStackTrace();
        }finally {
            try {
               reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

