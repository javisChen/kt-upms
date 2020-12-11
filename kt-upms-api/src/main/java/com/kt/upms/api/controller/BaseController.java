package com.kt.upms.api.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.component.dto.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public abstract class BaseController {

    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;


    protected HttpSession getSession() {
        return session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    protected Page getPage(PageRequest pageRequest) {
        return new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
    }

    /**
//     * 导出文件（基于Bean）
//     * @param filename 文件名
//     * @param list 导出数据
//     * @param headers excel头
//     * @param fields 数据对应的字段
//     * @throws IOException
//     */
//    protected void export(String filename, List<?> list, String[] headers, String[] fields) throws IOException {
//        ExcelUtils.exportToResp(filename, list, headers, fields);
//    }
//
//    /**
//     * 导出文件（基于Bean）
//     * @param filename 文件名
//     * @param list 导出数据
//     * @param headers excel头
//     * @param fields 数据对应的字段
//     * @param sheetName 表名
//     * @throws IOException
//     */
//    protected void export(String filename, List<?> list, String[] headers, String[] fields, String sheetName) throws IOException {
//        ExcelUtils.exportToResp(filename, list, headers, fields, sheetName);
//    }
//
//    /**
//     * 导出文件（基于List<Map>）
//     * @param filename 文件名
//     * @param list 导出数据
//     * @param headers excel头
//     * @param fields 数据对应的字段
//     * @throws IOException
//     */
//    protected void exportFromMap(String filename, List<Map<String, Object>> list, String[] headers, String[] fields) throws IOException {
//        ExcelUtils.exportMapToResp(filename, list, headers, fields);
//    }
//
//    /**
//     * 导出文件（基于List<Map>）
//     * @param filename 文件名
//     * @param list 导出数据
//     * @param headers excel头
//     * @param fields 数据对应的字段
//     * @param sheetName 表名
//     * @throws IOException
//     */
//    protected void exportFromMap(String filename, List<?> list, String[] headers, String[] fields, String sheetName) throws IOException {
//        ExcelUtils.exportMapToResp(filename, list, headers, fields, sheetName);
//    }
//
//    /**
//     * 导出模板
//     * @param filename 文件名
//     * @param headers excel头
//     */
//    protected void exportTemplate(String filename, String[] headers) throws IOException {
//        ExcelUtils.exportMapToResp(filename, null, headers,null, "模板");
//    }

}
