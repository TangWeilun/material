package org.material.management.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.material.management.model.tablemodel.MaterialCategoryModel;
import org.springframework.http.MediaType;
import org.material.management.service.MaterialInfoService;
import org.material.management.model.propertymodel.purchaseandstore.PurchaseAndStoreList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.material.management.model.tablemodel.MaterialBaseModel;

@RestController
@RequestMapping("/MaterialManagement")
@Api(value = "物料信息接口", description = "物料信息接口")
public class MaterialInfoController {
    @Autowired
    MaterialInfoService materialInfoService;

    @Autowired
    PurchaseAndStoreList purchaseAndStoreList;

    @PostMapping(value = "/getBaseInfo")
    @ApiOperation(value = "根据给定参数查询基础信息", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MaterialBaseModel> getBaseInfo (@RequestBody Map<String, Object> params) {
        // 参数必须非空！
        assert(params.size() > 0);
        return materialInfoService.getBaseInfoByParams(params);
    }

    @PostMapping(value = "/getMaterialInfo")
    @ApiOperation(value = "根据给定参数查询物料信息", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @SuppressWarnings("unchecked")
    // 如果list和hashmap的转换会出问题，则会抛出异常
    public List<Object> getMaterialInfo (@RequestBody Map<Object, Object> params) {
        try {
            String spuCode = (String) params.get("spuCode");
            String spuName = (String) params.get("spuName");
            List<Integer> typeArr = (List<Integer>) params.get("typeArr");
            // 先不考虑组织编码
            int orgnizationId = 1;
            // 需要查询物料ID和物料分类ID
            return materialInfoService.getMaterialInfo(spuCode, spuName, typeArr, orgnizationId);
        } catch (ClassCastException e) {
            e.printStackTrace();
            List<Object> result = new ArrayList<>();
            result.add("请检查输入格式是否正确！");
            return result;
        }
    }


    @PostMapping(value = "/getMaterialCategory")
    @ApiOperation(value = "获取当前物料分类信息",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MaterialInfoService.MaterialCategoryTree getMaterialCategory(){
        return materialInfoService.getMaterialCategory();
    }

    @PostMapping(value = "/addMaterialCategory")
    @ApiOperation(value = "增加物料分类信息", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //返回1为成功添加数据，返回0为失败
    public int addMaterialCategory(@RequestBody Map<String, Object> params) {
        //要求code,name,parentId信息全部获取
        if(params.containsKey("code")&&params.containsKey("name")&&params.containsKey("parentId")){
            String code=(String)params.get("code");
            String name=(String)params.get("name");
            int parentId = (int)params.get("parentId");
            return materialInfoService.addMaterialCategory(code,name,parentId);
        } else return 0;
    }

    @PostMapping(value = "/updateMaterialCategory")
    @ApiOperation(value = "根据物料oldName及parentId更新newName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //返回1为成功更新数据，返回0为失败
    public int updateMaterialCategory(@RequestBody Map<String,Object> params) {
        //确保三个属性值全部获取
        if(params.containsKey("newName")&&params.containsKey("oldName")&&params.containsKey("parentId")){
            String newName=(String)params.get("newName");
            String oldName=(String)params.get("oldName");
            int parentId = (int)params.get("parentId");
            return materialInfoService.updateMaterialCategory(newName,oldName,parentId);
        } else return 0;
    }

    @PostMapping(value = "/deleteMaterialCategory")
    @ApiOperation(value = "删除物料分类编码信息", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int deleteMaterialCategory(@RequestBody Map<String,Object> params) {
        if(params.containsKey("name")&&params.containsKey("parentId")){
            String name = (String)params.get("name");
            int parentId = (int)params.get("parentId");
            return materialInfoService.deleteMaterialCategory(name,parentId);
        } else return 0;
    }
}