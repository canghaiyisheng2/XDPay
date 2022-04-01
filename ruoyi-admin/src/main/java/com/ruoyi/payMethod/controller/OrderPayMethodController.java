package com.ruoyi.payMethod.controller;

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
import com.ruoyi.payMethod.domain.OrderPayMethod;
import com.ruoyi.payMethod.service.IOrderPayMethodService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单支付方式信息Controller
 * 
 * @author ruoyi
 * @date 2022-03-26
 */
@RestController
@RequestMapping("/payMethod/payMethod")
public class OrderPayMethodController extends BaseController
{
    @Autowired
    private IOrderPayMethodService orderPayMethodService;

    /**
     * 查询订单支付方式信息列表
     */
    @PreAuthorize("@ss.hasPermi('payMethod:payMethod:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderPayMethod orderPayMethod)
    {
        startPage();
        List<OrderPayMethod> list = orderPayMethodService.selectOrderPayMethodList(orderPayMethod);
        return getDataTable(list);
    }

    /**
     * 导出订单支付方式信息列表
     */
    @PreAuthorize("@ss.hasPermi('payMethod:payMethod:export')")
    @Log(title = "订单支付方式信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderPayMethod orderPayMethod)
    {
        List<OrderPayMethod> list = orderPayMethodService.selectOrderPayMethodList(orderPayMethod);
        ExcelUtil<OrderPayMethod> util = new ExcelUtil<OrderPayMethod>(OrderPayMethod.class);
        util.exportExcel(response, list, "订单支付方式信息数据");
    }

    /**
     * 获取订单支付方式信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('payMethod:payMethod:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(orderPayMethodService.selectOrderPayMethodById(id));
    }

    /**
     * 新增订单支付方式信息
     */
    @PreAuthorize("@ss.hasPermi('payMethod:payMethod:add')")
    @Log(title = "订单支付方式信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderPayMethod orderPayMethod)
    {
        return toAjax(orderPayMethodService.insertOrderPayMethod(orderPayMethod));
    }

    /**
     * 修改订单支付方式信息
     */
    @PreAuthorize("@ss.hasPermi('payMethod:payMethod:edit')")
    @Log(title = "订单支付方式信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderPayMethod orderPayMethod)
    {
        return toAjax(orderPayMethodService.updateOrderPayMethod(orderPayMethod));
    }

    /**
     * 删除订单支付方式信息
     */
    @PreAuthorize("@ss.hasPermi('payMethod:payMethod:remove')")
    @Log(title = "订单支付方式信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(orderPayMethodService.deleteOrderPayMethodByIds(ids));
    }
}
