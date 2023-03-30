package com.baomidou.mybatisplus.samples.crud.utils;
 
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
 
 
@Slf4j
public class CsvUtil {
    /**
     * 解析csv文件并转成bean
     * @param file csv文件
     * @param clazz 类
     * @param <T> 泛型
     * @return 泛型bean集合
     */
    public static <T> List<T> getCsvData(File file, Class<T> clazz) {
        InputStreamReader in = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            in = new InputStreamReader(fileInputStream, "utf8");
        } catch (Exception e) {
            log.error("读取csv文件失败！");
        }
 
        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(clazz);
 
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(in)
                .withSeparator(',')
                .withQuoteChar('\'')
                .withMappingStrategy(strategy).build();
        return csvToBean.parse();
    }
}