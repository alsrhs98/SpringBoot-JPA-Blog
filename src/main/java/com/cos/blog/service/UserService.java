package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service // Spring이 컴포넌트 스캔을 통해서 Bean에 등록을 gowna .IoC를 해준다.
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User 회원찾기(String username){
        User user = userRepository.findByUsername(username).orElseGet(()->{
            return new User();
        });
        return user;
    }

    @Transactional
    public void 회원가입(User user) {

        String rawPassword = user.getPassword(); //원문
        String encPassword = encoder.encode(rawPassword); //해쉬
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user);
    }

    @Transactional
    public void 회원수정(User user) {
        // 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화시키고, 영속화된 User오브젝트를 수정
        // select를 해서 User 오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해서
        // 영속화된 오브젝트를 변경하면 자동으로DB에 update문을 날려주세요.

        User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
            return new IllegalArgumentException("찾기 실패");
        });

        String rawPassword = user.getPassword();
        String encPassword= encoder.encode(rawPassword);
        persistance.setPassword(encPassword);
        persistance.setEmail(user.getEmail());


        //회원수정 함수 종료시 == 서비스 종료 == 트랜잭션 종료 == commit이 자동으로 됨.
        // 더티체킹이 되어 변화된 것에 update문을 자동으로 날려줌

        //세션 등록




    }

//    @Transactional(readOnly = true)//Select할때 트랜잭션 시작. 서비스 종료시에 트랜잭션 종료(정합성)

}
