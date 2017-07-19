import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.StringUtils
import vn.vnptnet.archetype.warconfigurable.security.JWTChecker
import vn.vnptnet.archetype.warconfigurable.util.GroovyStreamTemplate
import javax.servlet.http.HttpServletRequest

HttpServletRequest req = request

def token = req.getParameter('token')
def message = "token is valid"
def header = ""
def payload = ""
if (token != null && token != "") {
    try {
        DecodedJWT jwt = JWTChecker.getInstance().verify(token)

        def decodeBase64 = { s -> StringUtils.newStringUtf8(Base64.decodeBase64(s)) }

        def aToken = token.split('\\.')
        header = decodeBase64(aToken[0])
        payload = decodeBase64(aToken[1])
    } catch (UnsupportedEncodingException exception){
        //UTF-8 encoding not supported
        message="bad encoding"
    } catch(TokenExpiredException exception){
        message="token is expired"
    } catch (JWTVerificationException exception){
        message="token is invalid"
    } catch (Exception exception){
        exception.printStackTrace()
        message = exception.getMessage()
    }
}

println GroovyStreamTemplate.html([
        "title":"Kiểm tra token",
        "layout":"_layout",
        "body":"token/validate_token",
        "token":token, "header":header, "payload":payload, "message": message
])