package com.ruoyi.payTxnJnl.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.payTxnJnl.domain.PayTxnJnl;
import com.ruoyi.payTxnJnl.service.IPayTxnJnlService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 支付流水Controller
 * 
 * @author ruoyi
 * @date 2022-03-26
 */
@RestController
@RequestMapping("/payTxnJnl/payTxnJnl")
public class PayTxnJnlController extends BaseController
{
    @Autowired
    private IPayTxnJnlService payTxnJnlService;

    /**
     * 查询支付流水列表
     */
    @PreAuthorize("@ss.hasPermi('payTxnJnl:payTxnJnl:list')")
    @GetMapping("/list")
    public TableDataInfo list(PayTxnJnl payTxnJnl)
    {
        startPage();
        List<PayTxnJnl> list = payTxnJnlService.selectPayTxnJnlList(payTxnJnl);
        return getDataTable(list);
    }

    /**
     * 导出支付流水列表
     */
    @PreAuthorize("@ss.hasPermi('payTxnJnl:payTxnJnl:export')")
    @Log(title = "支付流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PayTxnJnl payTxnJnl)
    {
        List<PayTxnJnl> list = payTxnJnlService.selectPayTxnJnlList(payTxnJnl);
        ExcelUtil<PayTxnJnl> util = new ExcelUtil<PayTxnJnl>(PayTxnJnl.class);
        util.exportExcel(response, list, "支付流水数据");
    }

    /**
     * 获取支付流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('payTxnJnl:payTxnJnl:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(payTxnJnlService.selectPayTxnJnlById(id));
    }

    /**
     * 新增支付流水
     */
    @PreAuthorize("@ss.hasPermi('payTxnJnl:payTxnJnl:add')")
    @Log(title = "支付流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PayTxnJnl payTxnJnl)
    {
        return toAjax(payTxnJnlService.insertPayTxnJnl(payTxnJnl));
    }

    /**
     * 修改支付流水
     */
    @PreAuthorize("@ss.hasPermi('payTxnJnl:payTxnJnl:edit')")
    @Log(title = "支付流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PayTxnJnl payTxnJnl)
    {
        return toAjax(payTxnJnlService.updatePayTxnJnl(payTxnJnl));
    }

    /**
     * 删除支付流水
     */
    @PreAuthorize("@ss.hasPermi('payTxnJnl:payTxnJnl:remove')")
    @Log(title = "支付流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(payTxnJnlService.deletePayTxnJnlByIds(ids));
    }
}
