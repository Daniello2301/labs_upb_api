package co.edu.upb.labs_upb.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

//    @Value("${security.jwt.secret-key}")
//    private String secret;

    private final static String SECRET_KEY_TWO = "3A722CCEFD159D68FA3914473F28C";

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }


    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username  = extractUserEmail(token);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY_TWO);
        return Keys.hmacShaKeyFor(keyBytes);
    }


//    private final static String SECRET_KEY = "t7gK6ALtFQ83D7ge8FZECGyX1Q+wu9AjCX+WfMn+hleXe7xM0bjK4T5j/v856q55\n" +
//            "CyTJSS7fF+Dsc+trNDGNMhQGKiAnt8OQLusD3I1H2iGYOhEeLcuqzsSWw/dvduaw\n" +
//            "J9KHKDzp5bbnqnU4mZDVQFeL0PgjlmmWjdZtna+ygYEge5EdlTijpFXEWPT6QM+M\n" +
//            "9YNAoGsiCCXyL2+Nnida2fsaYxHqz6fct2/SSQD9Yn5zSD2ssEqK6/cbgkvPu0XC\n" +
//            "gveuWY+bNXMwORIB6rls7icDDuhl32PwVte/FTfbO4K5QGGPGT32R/RRWdzFVulf\n" +
//            "ihGrYmiEAHeaiIV7ciC4mKYoT7bUODG02WzVN+17C7igegTuVDXioxTLKYSOd6Ab\n" +
//            "vf22cEt5xtWYEMe9WAZySI7RSZMLW7VJfuqSo0qbDSMA8mhQuJwWvIlMg8WdkPqa\n" +
//            "mbfeDErzzWiATlaULpB5dqsmoizJ4rE+NrBBGTNlWpRY0R4dchQD7kKPWkWtxXuy\n" +
//            "kut2vLq8kLZp3XUs9OQ2Su+gL67Z22N+uwl0SVko7/HshD5KGL+rkmO8SxowqC9x\n" +
//            "ouYtmI3d9CHGeME4flKtm46k5jtQYV9c6y+qQGIRzrd+4djw9GVgIztNqyozyx/O\n" +
//            "BE6VA4HlL/ZWrQUZxSajtxaKkZqbPs2JGeQD0Vn4PtOPGtEUfBWkRV4u8EpXd3SU\n" +
//            "mcRAN5vysQ9SuOTCKfyrfGETuBc7tbGFUqcW7gx/msLycekPNjr8cX28u53UDWqs\n" +
//            "bcF+a1dnd3fhFuOCzWxXyOIIUeUwbe3vdzep4J304YZTaJ+6POdPu9MJOZFzC5lF\n" +
//            "iOklT+0+DnwZdRkTCOM3lktESgT2EMdEZhjpJreImKii3eZDh2mmsMv1BbSuWcxa\n" +
//            "sg3BJrn05QUXFIWXE/9WWJvPg4gLsI4OuksGH5QN15dWgv3XUS7wvORZ6OCUeTGX\n" +
//            "ZSoVIEG2YaDgRhISj73RJS3vfkYhMxZD544tMEkWQZLYPdW8NYaFD1VSowjQIwbS\n" +
//            "+JmIDwPlf7DNPOF8276j43zPRPJ70pAsI03p1jjvJXjzH4psCmvQ8mXju7XElwrs\n" +
//            "LjqbUmBE31QfHLxkm7HIjcHeIMMs+zWaN6+VWWS2uKjUNS/S9Ul4TeTwm2mzrmsn\n" +
//            "vVfzpum4dqGTUXbINU66Gz8D09cVMejbglZ+rjtFR7dxy/Gm7M8B1EHM1XEc2MuN\n" +
//            "aVkysWMPU0G1vpnr3Z317Y7K0Eo96uC54TDSwQixCiremQry4+SloJ4S6wOu5COV\n" +
//            "RXTie26b+bv5Q6121pQ2WtUezGAlSo8clwePBtmYD8aboRSy7L/mQT6263t37hzV\n" +
//            "ze4vHK1qRmGbCoC+ARtjtYCgXFAzTe2yrpTcqlsawkwRUoeyyKe4tJ6CmtzstOyZ\n" +
//            "YYz6zTVueq/ZpEtiEEAQFUVOgrFCEijhnX/tloOvo2/ZzqsKcsCsXenoUtkY1aoO\n" +
//            "BPf2VYKi4wux2QyOKBuN5ZOmHyE768tFCZzXaHjQtvUWw4bYtfPlKNdmCLdkOhpD\n" +
//            "GSTQNJs1kWkaqIA40vYbdJgAHHibscznPqgHrKioYGhM2XrRQ7DTAzgF5k3X3EBk\n" +
//            "BJ6sf2D/QruvD08/AQQY1WUxQji5AKarDpJ2fo23g/8KZU0apdr7yKgmFjcryBet\n" +
//            "5/GMnwuzLFkdYv9LW77XUe/kRmmD+GstymuBkdPgWplycSfxgQA1PjjGxGGwlQlY\n" +
//            "Mbkbo+jAztYZp37GMRTkcLWAEkPqHbYyJsWk/HlTUHXZrVVdFPR/LOOfC7X4Sz2z\n" +
//            "HeeRj974HFRx9g7Tt4eVnXCoJh6lG7TlgK6RUvzxDzOqCPH3ekqlfMP7iDeM5YTW\n" +
//            "RYe3DjAIHyHGih9mk8yB/TuyPTatypOf8IuXAQTsHzEzBvixJRyCZ7AYyt/k6NfR\n" +
//            "nxW+SfDQmJOBAPM4UiCXNNq5mryMgulQzixjUzBfOqOIO89xh/Nq581LAQKBCd2Z\n" +
//            "zusegxQ0V1uiu1VotFAmowcM1DUEZAcnXYAxyNBsxIIsH34PVT6YRfsP/gXLyEqD\n" +
//            "iwJ2oIqyTacNrumgCpDgucSQA22ov6+Aexm52nbQ2y9Eo95ylChuQruoGH0V+c1d\n" +
//            "fS+ZPn0wnHp2n8ZUZcKajCu/z9/Blfw1IPcihCQr7F8lMQsz3QHbTsa1OvTTpiGt\n" +
//            "3wrIgOyZkSH+byywwP0wmvYhzLZj2ot1Tye0TuwwyByt1rlsb5D3DM6ptTQeAIAg\n" +
//            "oiyzT5LmJd/tpL+VBnEhpnhfGMPp/Zre3h4dkSFuFM3jXqPw536fR/32l2SYMCm1\n" +
//            "9HcK8bGxD8jny68UPbMs0rIvZ109qfT2lo/Px2yru1jkq3cchlfKgbeoNyAUMTjJ\n" +
//            "ViiZkof+O6FEx1Men6oDgDbSr9rECrcehWqMiM92lvlWc2yQv8RNRYaMyl4HRhJb\n" +
//            "jvYcy8N0tcDTWwgBVuApnq3bqZx8s2mY1rl6b3OlMugdDtxvYs7qAYSY24sb5aQL\n" +
//            "RF4IMLqpGiVVayniVOr62JOVDvMCDULKDz3C2AVdcLTa2rdM7bqc4vqZgNLmnfec\n" +
//            "Ksi5RTw+WqNm8FOAZNGFHQ8RTXynRsD92GUyY3vEaHRpjdY4hQtHJvT4x0mP12d3\n" +
//            "5JTLCMiNlr/mxMGUbFxP30f4S1o5EHZ/rV+ne8tBjFkVbJwOqBXepisZ1lNb+P3z\n" +
//            "NLt/LhPM9psSUi/aLtSexhrq7AeJ364M5bnLXu1gjtFw5v+qQkdp/iKq8WfOAmVh\n" +
//            "/Fm/j55Ag8BJmaYvqjQlCCdHmDDaoH7rmV2ECIQ7DNWZdJUc0ZK4j/rKghF3qf0o\n" +
//            "sSj1QxOV2YRorGw0Rtf+AOQDXunAaVaEZZX0w8v7O4ztll+giOoKjJe1HbOLkSbt\n" +
//            "MiEkgC6vt8gmbEXqADCG/cpIvd9vXmL6nbiyr5vSYUY8xJiBWfo4tQH4U8R379g8\n" +
//            "PXV5DwzPUlB3ETisaX/3zRX4hQq/D91UISRXn2M/0K1KQzRpsxrWOTXD5ktuvAyL\n" +
//            "tAfjHVy53ibXTIKX6CaHySM9TBI/csI9nNH9pmruUzKolXQnpMltwwPfXjXnj3t7\n" +
//            "9pRzwgTFxCu2D/EAvcLuo56N4JzbAFQXHDDmxJc/+0bcfdvi9+Jw1/DVqMcMNtrL\n" +
//            "aFH+fLOF/mEsQU3rdIOa/dMWjnZtQ103FzPRdnA81l9CFj7y3JwT99971IazjfjN\n" +
//            "p1w6bbVQU+rTrO6t5B6S3tEOxwltIlaZFtzvmabXiaDHQGM8CJYEx0jzYRx3D29q\n" +
//            "afosifbFr8xE0BAIP1HMhpR3StJDW8SCKcneEfRUhIigs+mcmOEEKpvogyiFAqZ6\n" +
//            "UN4TKUfCt0NF5FQUUXNK1yoe6QOIeocFj24dryfb1f212G5b/G8DpIb2FTw7VL9q\n" +
//            "6RzctBbqzeKcjVco9JX6y6FoUDlmr8RcQ2pUc3F+KTv/QKVke9J1f9Yb5qklvPyx\n" +
//            "kmE4t9KYhufWz6zhYPZPrx3SPaWCw/Y5XuoBSe1lt2u2ZNZdU5vw13t0Pw6sdELI\n" +
//            "qI712ySem/V4RChEs3UogJXo77Nyjfk7pp8kBGDl+LY1OGWmo0tfjxSFCN8anOWW\n" +
//            "4lDUrrJ5qzuFRW+cndpKBVHQq/cXkhNP1iMxZFOxbOz5AqA83dO6zeElItBWiore\n" +
//            "WHtxArX1Liz/TStJqJNIkbqEkeFWv1MxV9kcZu8X0nIhe8KZVGHnBMEOfiQgLVJ8\n" +
//            "/QA6J0zDSi0PhLCtqv+ca1ijYPKt0KH7W48EKiphhFMcOzo4tiLPZ2iLSQTt6wR2\n" +
//            "GHZgRGrXwviBBA6Npw+gh5dpV5gelQqVkpZRHenEbdeoso2udSJfDrBVwb1KY3kf\n" +
//            "hx58/k3fpABaS1H24RKzDzxHZGS0XlIt6TNuzUr5cYJE5HIK+sdojMiJgcIq17FD\n" +
//            "JDpC1TwpKDlk8ybz6ORy7kJpN98k1RgsO/oLiaDQTGYHZw7n5QZtRXxDgxvc8Vvd\n" +
//            "7LcnlcjboxrCaEoVm+UUNtv5UMosSQyQckVNtn5fT5GeYXI/2bOaSLacBSFGVHUh\n" +
//            "iCLjw5plzikwYSsqdDeiwspx3/35JM0E7kH3ZOn0D6c3RzYfELZV65Z7x2GtKUWc\n" +
//            "qhGNwviqiEtpCCXEsi/xsAkDGmmP8vsOcoE03wexNPPGqLTfABiGPKSCEhvzfACG\n" +
//            "sTJWajJcKnWNtKXVTxpw8uRN1q6mzrY47a3SS2TLvggx6WTZKt7e24SrVWL7vZov\n" +
//            "x7Gp+CUF8p8vXjsNC4mHDN3M9cwTbU3nXU8EfH4FA/JlZW4OERZFaGS4FZUivCgw\n" +
//            "a3TNxFG7id0Ckvf0ZpMeoaQ9cazO4S3ufyAX4zwIYucOhv1z1/6mb8HfqosHPcvW\n" +
//            "OURItmW7Cg1VL/jPoNfAkSJFfsNmZp2I5UsahlCgtBeWL2/qNuxvIZotqiY2VEQm\n" +
//            "pNgfbzm6pz3nEQ2oKKOvZ763MDdV+HlWigUj3O3VDPOg2zFiy9obNJekVvt1EMge\n" +
//            "sksbXoLO/Y/Y3CXSCnyy/0h21Rx61NKTmgPHznJ6BfDX/rIdcZ4Ua/6kGjhb4c/8\n" +
//            "BZBUHhyyfG5HVmviTTS6sXFK0LA/GG2wz15AQxO09VWUEEtqAo3coasyhPbnE9iT\n" +
//            "pEDIvswKwRH9W90i51XdHhtkrkAe45zXwmXq4bwUa4do3b6Yjp0aQaZOjXM3URg0\n" +
//            "FKUExHTFsTK4OG8EFCdrsDBTwbvmT5bfDOdouB8aTJDpJjcjnkW89awlZCoA4dZI\n" +
//            "q3PsRUzR0wa8dNsRw4mZIj4yq0oqMYvi7JtKePNF5q6pkpNtXOOvETYv7qtD5oWW\n" +
//            "dPatvi443vLUx5JuzWyuTBXsQrJO3QGJE0itQ7+v1oTDJmIK6wOhYjdRhM65Pn8x\n" +
//            "/+hfdONzMvhQCal2X0qPDIO6EmaDc0Gr1w9z1gNFGR6ANVJPlGDe/05IgiQAVGIn\n" +
//            "BoMpXUOwp4V1jB5Bn77GRypRWaACfPScux+NscNEIH9LJV7agLhmP5pWKIg3s8xh\n" +
//            "UbOBAxwvon1dRwJGP77TUQDR2zpVsuAdVDU9CN5wamXWkBKtZjQQ+Sr9w3jQBrHQ\n" +
//            "V2FgeaXLbiTwb/bh1Tbc/Lx9xBZr2bnlKNc6uHp7cPlJzRbTvxEwXu3nxeTD8LGf\n" +
//            "KbRSMBEPP/F7K1TMn2U9NvcLzUPoY7cPwwcaxQJi/eR5QiiM9SdsR59hWED6voce\n" +
//            "czcn0ibhPuozjTITw7J5IQdKAE0log8HkrNh1UUyU/uXE1vyXe+50U+kYkiqgeCr\n" +
//            "omOBRhWn5pOcl29X3+C2yiJjjE9YasqFnerZEpZJqjqe9o0C6IIPn5Y3s6GcgC4L\n" +
//            "go/CvbMUxiyuT4K4Z13DS0+lAIefarTYsJBOiGhCy6ejEremo5KB5hvXJUbv2uZ6\n" +
//            "CtCqgbMbZjvzIubE8NZLdpFiiZ2YyCIv1QA/v1RenmkrAD5My7xQfQPPpZb7pKSX\n" +
//            "asmTQk1n8gKjgc4uu6exB6MEeGOuPtvxMsd59ojpRDpzolGaM5DAM0W86xKk7jim\n" +
//            "RkJPFJ7oXdfL6k0klof7rzyTKlWrq3lzTUuJcW/UfXMHLlntf2ZgYvbvaS0ZfTG+\n" +
//            "lJ4h4BWyxRjkP36+WS8kD7xXMwQ7Si7JmwwroV792o+oZKhP+4pJajLCp6oyrcoh\n" +
//            "FUk79ZpEB3HIrJHWMC7uIC5nK+nA3ty5JXCNQF642RcLIzxamHMyzAEmVgksNvV+\n" +
//            "sITE6xMx+7cwomln8DxICxRhHMB/fohELVB7GLE0Fd1EWJvK7XQQPEZM1hYtrAHq\n" +
//            "HGWiXFWGrgIwRTgGqbpBRTNQtCraPjG13nG3X/W55mDvcML//lfOTM0JPTgIJl12\n" +
//            "uUUSNlfGIKhqaXbkJl8uXNTCbhqh+Ba5g+SJt895cSZaMckEt9fPz1Pz53aNf2JQ\n" +
//            "cRR99Wwv+BHBBW35FzvBhllNeumZMTwTutol2U05Y/hCsULLHWkE7Mk56Z3SS2s/\n" +
//            "2mFPSSl8Qj4Tsw7ex306RUR6lA3XloxbklpzLM0iMY4T38lWq2aFdwaTIGctMjCI\n" +
//            "dAzkJ6v7BTUNoLN7yg3mGsAynE1uV+m2sku9gnV3/aYZ4Pse8aFTEP/W5DafRRcP\n" +
//            "RAw0Z6x1MCMFKogLrsJr9Xz6qGDCiHfHQaJ1XHz8XEMqOVkeWHrNhDEQhbmD1Ff/\n" +
//            "JUFindB7qaa93hC0qCzdi3HEjMEBGgcyYESVvIK/WRJwzWUieejV/G7SubRw/dhf";
}
