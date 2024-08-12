package com.hsk.library_mgmt_backend;

import com.hsk.library_mgmt_backend.persistent.entity.Member;
import com.hsk.library_mgmt_backend.service.MemberService;
import com.hsk.library_mgmt_backend.web.v1.payload.member.MemberRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.hsk.library_mgmt_backend.web.v1.enums.Role.ADMIN;

@SpringBootApplication
public class LibraryMgmtBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryMgmtBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(MemberService service) {
        return args -> {
            String adminEmail = "admin@zit.com";
            Member existingAdmin = service.getMemberByEmail(adminEmail);
            if (existingAdmin == null) {
                var admin = new MemberRequest("Admin", adminEmail, "9876543210", ADMIN, "test@123");
                System.out.println("Admin created with email "+admin.email() +" and password test@123" );
            }
        };
    }
}
