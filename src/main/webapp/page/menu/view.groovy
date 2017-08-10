import groovy.text.StreamingTemplateEngine
import groovy.text.markup.MarkupTemplateEngine

import javax.servlet.RequestDispatcher
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

HttpServletRequest httpreq = request
HttpServletResponse httpres = response
ServletRequest req = request
ServletResponse res = response


def html = """
<div>
<h1>Simple menu</hi>
</div>
"""
def engine = new StreamingTemplateEngine()
def template = engine.createTemplate(html)
return template.make().toString()