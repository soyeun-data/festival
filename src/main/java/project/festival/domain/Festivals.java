package project.festival.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
public class Festivals {

    @Id
    private Long id;
    private String fstvl_nm;
    private String opar;
    @Temporal(TemporalType.DATE)
    private Date fstvl_start_date;
    @Temporal(TemporalType.DATE)
    private Date fstvl_end_date;
    private String fstvl_co;
    private String mnnst_nm;
    private String auspc_instt_nm;
    private String suprt_instt_nm;
    private String phone_number;
    private String homepage_url;
    private String relate_info;
    private String rdnmadr;
    private String lnmadr;
    private String latitude;
    private String longitude;
    private String reference_date;
    private String instt_code;
    private String instt_nm;

}
