package com.grizzly.vendormicro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorDTO {
    private String vendorId;
    private String name;
    private String contactNum;
    private String website;
    private String email;
    private String bio;

    public Vendor toEntity() {
        Vendor vendor = new Vendor(this.name, this.contactNum, this.website, this.email, this.bio);
        return vendor;
    }
}
