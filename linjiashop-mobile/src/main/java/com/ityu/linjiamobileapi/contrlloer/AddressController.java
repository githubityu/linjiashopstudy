package com.ityu.linjiamobileapi.contrlloer;

import com.ityu.bean.entity.shop.Address;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.service.shop.AddressService;
import com.ityu.utils.Lists;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 11/5/2019 7:36 PM
 */
@RestController
@RequestMapping("/user/address")
public class AddressController extends BaseController {
    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Object get(@PathVariable("id") Long id) {
        Long idUser = getAdminUser().getId();
        Address address = addressService.get(idUser, id);
        return Rets.success(address);

    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Object remove(@PathVariable("id") Long id) {
        Long idUser = getAdminUser().getId();
        addressService.delete(idUser, id);
        return Rets.success();
    }

    @RequestMapping(value = "{id}/{isDefault}", method = RequestMethod.POST)
    public Object changeDefault(@PathVariable("id") Long id, @PathVariable("isDefault") Boolean isDefault) {
        Long idUser = getAdminUser().getId();
        Address defaultAddr = addressService.getDefaultAddr(idUser);
        if (defaultAddr != null) {
            if (defaultAddr.getId().intValue() == id.intValue()) {

                defaultAddr.setIsDefault(isDefault);
                addressService.update(defaultAddr);
                return Rets.success();

            } else {
                if (isDefault) {
                    defaultAddr.setIsDefault(false);
                    addressService.update(defaultAddr);
                }
            }
        }

        Address address = addressService.get(idUser, id);
        address.setIsDefault(isDefault);
        addressService.update(address);
        return Rets.success(address);

    }

    @RequestMapping(value = "/queryByUser", method = RequestMethod.GET)
    public Object getByUser() {
        Long idUser = getAdminUser().getId();
        List<SearchFilter> filters = Lists.newArrayList(
                SearchFilter.build("idUser", SearchFilter.Operator.EQ, idUser),
                SearchFilter.build("isDelete", SearchFilter.Operator.EQ, false)
        );
        List<Address> list = addressService.queryAll(filters);
        return Rets.success(list);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Object save(@RequestBody @Valid Address addressInfo) {
        Long idUser = getAdminUser().getId();
        addressInfo.setIdUser(idUser);
        if (addressInfo.getId() != null) {
            Address old = addressService.get(idUser, addressInfo.getId());
            addressInfo.setCreateTime(old.getCreateTime());
            addressService.update(addressInfo);
        } else {
            addressService.insert(addressInfo);
        }
        return Rets.success(addressInfo);
    }

}
