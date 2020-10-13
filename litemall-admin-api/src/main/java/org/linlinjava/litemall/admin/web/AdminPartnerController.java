package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallAd;
import org.linlinjava.litemall.db.domain.LitemallPartner;
import org.linlinjava.litemall.db.service.LitemallAdService;
import org.linlinjava.litemall.db.service.LitemallPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin/partner")
@Validated
public class AdminPartnerController {
    private final Log logger = LogFactory.getLog(AdminPartnerController.class);

    @Autowired
    private LitemallPartnerService partnerService;

    @RequiresPermissions("admin:partner:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "合作商管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String name, String telephone,String address,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LitemallPartner> partnerList = partnerService.querySelective(name, telephone,address, page, limit, sort, order);
        return ResponseUtil.okList(partnerList);
    }

    private Object validate(LitemallPartner partner) {
        String name = partner.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        String telephone = partner.getTelephone().toString();
        if (StringUtils.isEmpty(telephone)) {
            return ResponseUtil.badArgument();
        }
        String content = partner.getAddress();
        if (StringUtils.isEmpty(content)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("admin:partner:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "合作商管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody LitemallPartner partner) {
        Object error = validate(partner);
        if (error != null) {
            return error;
        }
        partnerService.add(partner);
        return ResponseUtil.ok(partner);
    }

    @RequiresPermissions("admin:partner:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "合作商管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        LitemallPartner ad = partnerService.findById(id);
        return ResponseUtil.ok(ad);
    }

    @RequiresPermissions("admin:partner:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "合作商管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody LitemallPartner partner) {
        Object error = validate(partner);
        if (error != null) {
            return error;
        }
        if (partnerService.updateById(partner) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(partner);
    }

    @RequiresPermissions("admin:partner:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "合作商管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody LitemallPartner partner) {
        Integer id = partner.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        partnerService.deleteById(id);
        return ResponseUtil.ok();
    }

}
