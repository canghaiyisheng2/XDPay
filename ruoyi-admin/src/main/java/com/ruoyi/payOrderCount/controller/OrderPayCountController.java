package com.ruoyi.payOrderCount.controller;

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
import com.ruoyi.payOrderCount.domain.OrderPayCount;
import com.ruoyi.payOrderCount.service.IOrderPayCountService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单请求数记录Controller
 * 
 * @author ruoyi
 * @date 2022-03-24
 */
@RestController
@RequestMapping("/payOrderCount/payOrderCount")
public class OrderPayCountController extends BaseController
{
    @Autowired
    private IOrderPayCountService orderPayCountService;

    /**
     * 查询订单请求数记录列表
     */
    @PreAuthorize("@ss.hasPermi('payOrderCount:payOrderCount:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderPayCount orderPayCount)
    {
        startPage();
        List<OrderPayCount> list = orderPayCountService.selectOrderPayCountList(orderPayCount);
        return getDataTable(list);
    }

    /**
     * 导出订单请求数记录列表
     */
    @PreAuthorize("@ss.hasPermi('payOrderCount:payOrderCount:export')")
    @Log(title = "订单请求数记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderPayCount orderPayCount)
    {
        List<OrderPayCount> list = orderPayCountService.selectOrderPayCountList(orderPayCount);
        ExcelUtil<OrderPayCount> util = new ExcelUtil<OrderPayCount>(OrderPayCount.class);
        util.exportExcel(response, list, "订单请求数记录数据");
    }

    /**
     * 获取订单请求数记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('payOrderCount:payOrderCount:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(orderPayCountService.selectOrderPayCountById(id));
    }

    /**
     * 新增订单请求数记录
     */
    @PreAuthorize("@ss.hasPermi('payOrderCount:payOrderCount:add')")
    @Log(title = "订单请求数记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderPayCount orderPayCount)
    {
        return toAjax(orderPayCountService.insertOrderPayCount(orderPayCount));
    }

    /**
     * 修改订单请求数记录
     */
    @PreAuthorize("@ss.hasPermi('payOrderCount:payOrderCount:edit')")
    @Log(title = "订单请求数记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderPayCount orderPayCount)
    {
        return toAjax(orderPayCountService.updateOrderPayCount(orderPayCount));
    }

    /**
     * 删除订单请求数记录
     */
    @PreAuthorize("@ss.hasPermi('payOrderCount:payOrderCount:remove')")
    @Log(title = "订单请求数记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(orderPayCountService.deleteOrderPayCountByIds(ids));
    }
}
