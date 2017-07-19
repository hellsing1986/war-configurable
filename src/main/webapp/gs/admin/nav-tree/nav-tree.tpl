def _select = GroovyStreamTemplate.component("_select")

layout '../../gs/_layout_admin.tpl',
title: 'nav manager',
mainContent: contents {
    p("remoteHost = ${httpReq.getRemoteHost()}")
    newLine()
    fragment "${_select}", name: "demo-name"
}