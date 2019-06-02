package com.yrw.im.common.test

import com.yrw.im.common.domain.conn.Conn
import com.yrw.im.common.domain.conn.InternalConn
import com.yrw.im.common.domain.conn.MemoryConnContext
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.util.Attribute
import org.apache.commons.codec.language.ColognePhonetic
import spock.lang.Specification

/**
 * Date: 2019-06-02
 * Time: 16:04
 * @author yrw
 */
class ConnContextTest extends Specification {

    def "test internal conn"() {
        given:
        def ctx = Mock(ChannelHandlerContext) {
            channel() >> Mock(Channel) {
                attr(Conn.NET_ID) >> Mock(Attribute) {
                    get() >> givenNetId
                }
            }
        }
        InternalConn conn = new InternalConn(ctx)

        expect:
        conn.getCtx() == ctx
        conn.getNetId() == expectedNetId

        where:
        givenNetId     | expectedNetId
        "987986892353" | "987986892353"
        "agarteag"     | "agarteag"
    }

    def "test memory conn context"() {
        given:
        def netId = "987986892353";
        def ctx = Mock(ChannelHandlerContext) {
            channel() >> Mock(Channel) {
                attr(Conn.NET_ID) >> Mock(Attribute) {
                    get() >> netId
                }
            }
        }
        def context = new MemoryConnContext<InternalConn>()

        when:
        InternalConn conn = new InternalConn(ctx)
        context.addConn(conn)

        then:
        context.getConn(netId) == conn

        when:
        context.removeConn(netId)
        context.removeConn("123135135")

        then:
        context.getConn(netId) == null
    }
}
