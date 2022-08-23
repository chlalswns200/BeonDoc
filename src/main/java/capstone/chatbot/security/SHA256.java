package capstone.chatbot.security;

import java.security.MessageDigest;

public class SHA256 {

    public String SHA256Encrypt(String password){
        StringBuffer hexString = new StringBuffer(); // StirngBuffer 변수 생성

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // 암호화 라이브러리 불러오기
            byte[] hash = digest.digest(password.getBytes("UTF-8")); // 값 읽어 오기

            // 반복문을 통해 비밀번호 암호화
            for(int i=0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');

                hexString.append(hex);
            }

        }catch(SecurityException e){
            throw new IllegalStateException("SecurityException"); // 예외 발생시 출력
        }catch(Exception e){
            throw new IllegalStateException("SHA256Encrypt : 암호화 실패하였습니다."); // 예외 발생 시 출력
        }
        return hexString.toString().toUpperCase(); // 문자열로 변환이후 대문자로 변환후 반환
    }

}
