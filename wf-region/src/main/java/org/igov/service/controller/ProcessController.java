/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.igov.service.controller;

import com.google.common.base.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.igov.io.db.kv.statical.exceptions.RecordNotFoundException;

import org.igov.analytic.model.access.AccessGroup;
import org.igov.analytic.model.access.AccessUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.igov.analytic.model.process.Process;
import org.igov.analytic.model.process.ProcessTask;
import org.igov.analytic.model.attribute.Attribute;
import org.igov.analytic.model.attribute.AttributeDao;
import org.igov.analytic.model.attribute.AttributeType;
import org.igov.analytic.model.attribute.Attribute_File;
import org.igov.analytic.model.attribute.Attribute_FileDao;
import org.igov.analytic.model.attribute.Attribute_StingShort;
import org.igov.analytic.model.process.ProcessDao;
import org.igov.analytic.model.source.SourceDB;
import org.igov.io.db.kv.analytic.IBytesDataStorage;
import org.igov.io.db.kv.analytic.IFileStorage;
import org.igov.util.VariableMultipartFile;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author olga
 */
@Controller
@Api(tags = {"ProcessController - процессы и задачи"})
@RequestMapping(value = "/analytic/process")
public class ProcessController {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessController.class);

    private static final String JSON_TYPE = "Accept=application/json";

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private AttributeDao attributeDao;

    @Autowired
    private Attribute_FileDao attribute_FileDao;

    //@Autowired
    //private IBytesDataStorage durableBytesDataStorage;
    @Autowired
    private IFileStorage durableFileStorage;

    @ApiOperation(value = "/setProcess", notes = "##### Process - сохранение процесса #####\n\n")
    @RequestMapping(value = "/setProcess", method = RequestMethod.GET//, headers = {JSON_TYPE}
    )
    public @ResponseBody
    Process setSubject() { //@RequestBody Process oProcess
        LOG.info("/setProcess!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! :)");

        LOG.info("/setProcess!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! :)");
        Process process = new Process();
        Attribute attribute = new Attribute();
        Attribute_File attribute_File = new Attribute_File();
        SourceDB sourceDB = new SourceDB();
        AttributeType attributeType = new AttributeType();
        process.setId(new Long(1));
        process.setoDateStart(new DateTime());
        process.setoDateFinish(new DateTime());
        process.setoSourceDB(sourceDB);
        process.setsID_("test!!");
        process.setsID_Data("test!!");
        List<Attribute> attributes = new ArrayList();
        process.setaAttribute(attributes);
        attribute.setoAttributeType(attributeType);
        attribute_File.setsID_Data("test");
        attribute_File.setsFileName("test");
        attribute_File.setsContentType("pdf");
        attribute_File.setsExtName("txt");
        sourceDB.setId(new Long(1));
        attributeType.setId(new Long(7));

        try {
            attribute = attributeDao.saveOrUpdate(attribute);
            attributes.add(attribute);
            attribute_File.setoAttribute(attribute);
            attribute_File = attribute_FileDao.saveOrUpdate(attribute_File);
            attribute.setoAttribute_File(attribute_File);
            processDao.saveOrUpdate(process);
        } catch (Exception ex) {
            LOG.error("!!!!Eror: ", ex);
            process.setsID_(ex.getMessage());
        }
        return process;
    }

    private Process creatStubForSet() {
        LOG.info("/setProcess!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! :)");
        Process process = new Process();
        //ProcessTask processTask = new ProcessTask();
        Attribute attribute = new Attribute();
        //Attribute_StingShort attribute_StingShort = new Attribute_StingShort();
        Attribute_File attribute_File = new Attribute_File();
        //AccessGroup accessGroup = new AccessGroup();
        //AccessUser accessUser = new AccessUser();
        SourceDB sourceDB = new SourceDB();
        AttributeType attributeType = new AttributeType();
        //---------------------------
        process.setId(new Long(1));
        process.setoDateStart(new DateTime());
        process.setoDateFinish(new DateTime());
        process.setoSourceDB(sourceDB);
        process.setsID_("test!!");
        process.setsID_Data("test!!");

        //List<ProcessTask> tasks = new ArrayList();
        //tasks.add(processTask);
        //process.setaProcessTask(tasks);
        List<Attribute> attributes = new ArrayList();
        attributes.add(attribute);
        process.setaAttribute(attributes);
        //process.setaAttribute(attributes);
        //-------------------------------
        //processTask.setId(new Long(1));
        //processTask.setoDateStart(new DateTime());
        //processTask.setoDateFinish(new DateTime());
        //processTask.setsID_("test");
        //List<AccessGroup> accessGroups = new ArrayList();
        //accessGroups.add(accessGroup);
        //processTask.setaAccessGroup(accessGroups);
        //List<AccessUser> accessUsers = new ArrayList();
        //accessUsers.add(accessUser);
        //processTask.setaAccessUser(accessUsers);
        //------------------------------
        //attribute.setId(new Long(1));
        attribute.setoAttributeType(attributeType);
        //attribute.setoAttribute_StingShort(attribute_StingShort);
        attribute.setoAttribute_File(attribute_File);
        //------------------------------
        //attribute_StingShort.setId(new Long(1));
        //attribute_StingShort.setsValue("test");
        //------------------------------
        //attribute_File.setId(new Long(1));
        attribute_File.setsID_Data("test");
        attribute_File.setsFileName("test");
        attribute_File.setsContentType("pdf");
        attribute_File.setsExtName("txt");
        //-------------------------------
        //accessGroup.setId(new Long(1));
        //accessGroup.setsID("test");
        //--------------------------------
        //accessUser.setId(new Long(1));
        //accessUser.setsID("test");
        //--------------------------------
        sourceDB.setId(new Long(1));
        //sourceDB.setName("test");
        attributeType.setId(new Long(7));
        //attributeType.setName("test");

        return process;
    }

    //http://localhost:8080/wf-region/service/analytic/process/getProcesses?sID_=1
    @ApiOperation(value = "/getProcesses", notes = "##### Process - получение процесса #####\n\n")
    @RequestMapping(value = "/getProcesses", method = RequestMethod.GET, headers = {JSON_TYPE})
    public @ResponseBody
    List<Process> getProcesses(@ApiParam(value = "внутренний ид заявки", required = true) @RequestParam(value = "sID_") String sID_,
            @ApiParam(value = "ид источника", required = false) @RequestParam(value = "nID_Source", required = false) Long nID_Source) {
        LOG.info("/getProcess!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! :)");
        List<Process> result = new ArrayList();
        try {
            if ("1".equalsIgnoreCase(sID_.trim())) {
                result.add(creatStub());
            } else {
                List<Process> processes = processDao.findAll();
                LOG.info("processes: " + processes.size());
                for (Process process : processes) {
                    for (Attribute attribute : process.getaAttribute()) {
                        if (attribute.getoAttributeType().getId() == 7) { //file
                            Optional<Attribute_File> attribute_File = attribute_FileDao.findBy("oAttribute.id", attribute.getId());
                            if (attribute_File.isPresent()) {
                                attribute.setoAttribute_File(attribute_File.get());
                            }
                        }
                    }
                }
                result.addAll(processes);
            }
        } catch (Exception ex) {
            LOG.info("ex: " + ex);
            LOG.error("ex: ", ex);
            Process process = creatStub();
            process.setsID_(ex.getMessage());
            result.add(process);
        }
        return result;
    }

    private Process creatStub() {
        LOG.info("/setProcess!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! :)");
        Process process = new Process();
        ProcessTask processTask = new ProcessTask();
        Attribute attribute = new Attribute();
        Attribute_StingShort attribute_StingShort = new Attribute_StingShort();
        Attribute_File attribute_File = new Attribute_File();
        AccessGroup accessGroup = new AccessGroup();
        AccessUser accessUser = new AccessUser();
        SourceDB sourceDB = new SourceDB();
        AttributeType attributeType = new AttributeType();
        //---------------------------
        process.setId(new Long(1));
        process.setoDateStart(new DateTime());
        process.setoDateFinish(new DateTime());
        process.setoSourceDB(sourceDB);
        process.setsID_("test");
        process.setsID_Data("test");

        List<ProcessTask> tasks = new ArrayList();
        tasks.add(processTask);
        process.setaProcessTask(tasks);

        List<Attribute> attributes = new ArrayList();
        attributes.add(attribute);
        process.setaAttribute(attributes);
        process.setaAttribute(attributes);
        //-------------------------------
        processTask.setId(new Long(1));
        processTask.setoDateStart(new DateTime());
        processTask.setoDateFinish(new DateTime());
        processTask.setsID_("test");
        List<AccessGroup> accessGroups = new ArrayList();
        accessGroups.add(accessGroup);
        processTask.setaAccessGroup(accessGroups);
        List<AccessUser> accessUsers = new ArrayList();
        accessUsers.add(accessUser);
        processTask.setaAccessUser(accessUsers);
        //------------------------------
        attribute.setId(new Long(1));
        attribute.setoAttributeType(attributeType);
        attribute.setoAttribute_StingShort(attribute_StingShort);
        attribute.setoAttribute_File(attribute_File);
        //------------------------------
        attribute_StingShort.setId(new Long(1));
        attribute_StingShort.setsValue("test");
        //------------------------------
        attribute_File.setId(new Long(1));
        attribute_File.setsID_Data("test");
        attribute_File.setsFileName("test");
        attribute_File.setsContentType("pdf");
        attribute_File.setsExtName("txt");
        //-------------------------------
        accessGroup.setId(new Long(1));
        accessGroup.setsID("test");
        //--------------------------------
        accessUser.setId(new Long(1));
        accessUser.setsID("test");
        //--------------------------------
        sourceDB.setId(new Long(1));
        sourceDB.setName("test");
        attributeType.setId(new Long(1));
        attributeType.setName("test");

        return process;
    }

    //http://localhost:8080/wf-region/service/analytic/process/getFile?sID_Data=1
    @ApiOperation(value = "/getFile", notes = "##### File - получение контента файла #####\n\n")
    @RequestMapping(value = "/getFile", method = RequestMethod.GET, headers = {JSON_TYPE})
    public @ResponseBody
    byte[] getFile(@ApiParam(value = "внутренний ид заявки", required = true) @RequestParam(value = "nID_Attribute_File") Long nID_Attribute_File,
            HttpServletResponse httpResponse) throws RecordNotFoundException {
        //получение через дао из таблички с файлами файлов
        VariableMultipartFile multipartFile = null;
        try {
            Optional<Attribute_File> attribute_File = attribute_FileDao.findById(nID_Attribute_File);
            if (attribute_File.isPresent()) {
                Attribute_File file = attribute_File.get();
                multipartFile = new VariableMultipartFile(durableFileStorage.openFileStream(String.valueOf(file.getsID_Data())),
                        file.getsFileName(), file.getsFileName() + "." + file.getsExtName(), file.getsContentType());
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setHeader("Content-disposition", "attachment; filename=" + multipartFile.getName());
                //httpResponse.setHeader("Content-Type", "application/octet-stream");
                httpResponse.setHeader("Content-Type", multipartFile.getContentType());
                httpResponse.setContentLength(multipartFile.getBytes().length);
            }
            return multipartFile.getBytes();
        } catch (Exception ex) {
            LOG.error("!!!Error: ", ex);
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setHeader("Content-disposition", "attachment; filename=fileNotFound.txt"); //"Content-Disposition"
            //httpResponse.setHeader("Content-Type", "application/octet-stream");
            httpResponse.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");
            httpResponse.setContentLength(ex.getMessage().getBytes().length);
            return ex.getMessage().getBytes();
        }
    }

}
