package net.mamoe.mirai.qqandroid.network.protocol.packet.chat.image

import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.writeFully
import net.mamoe.mirai.data.Packet
import net.mamoe.mirai.qqandroid.QQAndroidBot
import net.mamoe.mirai.qqandroid.io.serialization.ProtoBufWithNullableSupport
import net.mamoe.mirai.qqandroid.io.serialization.writeProtoBuf
import net.mamoe.mirai.qqandroid.network.QQAndroidClient
import net.mamoe.mirai.qqandroid.network.protocol.data.proto.Cmd0x352Packet
import net.mamoe.mirai.qqandroid.network.protocol.data.proto.GetImgUrlReq
import net.mamoe.mirai.qqandroid.network.protocol.data.proto.UploadImgReq
import net.mamoe.mirai.qqandroid.network.protocol.packet.OutgoingPacket
import net.mamoe.mirai.qqandroid.network.protocol.packet.OutgoingPacketFactory
import net.mamoe.mirai.qqandroid.network.protocol.packet.buildOutgoingUniPacket
import net.mamoe.mirai.utils.io.debugPrintThis

internal class LongConn {

    internal object OffPicUp : OutgoingPacketFactory<OffPicUp.ImageUpPacketResponse>("LongConn.OffPicUp") {

        operator fun invoke(client: QQAndroidClient, req: UploadImgReq): OutgoingPacket {
            return buildOutgoingUniPacket(client) {
                //  val data = ProtoBufWithNullableSupport.dump(
//
                //  )
                // writeInt(data.size + 4)
                writeProtoBuf(
                    Cmd0x352Packet.serializer(),
                    Cmd0x352Packet.createByImageRequest(req)
                )
            }
        }

        override suspend fun ByteReadPacket.decode(bot: QQAndroidBot): ImageUpPacketResponse {
            this.debugPrintThis()
            TODO()
        }


        sealed class ImageUpPacketResponse : Packet {
            object Success : ImageUpPacketResponse()
        }

    }

    object OffPicDown : OutgoingPacketFactory<OffPicDown.ImageDownPacketResponse>("LongConn.OffPicDown") {
        operator fun invoke(client: QQAndroidClient, req: GetImgUrlReq): OutgoingPacket {
            return buildOutgoingUniPacket(client) {
                val data = ProtoBufWithNullableSupport.dump(
                    Cmd0x352Packet.serializer(),
                    Cmd0x352Packet.createByImageRequest(req)
                )
                writeInt(data.size + 4)
                writeFully(data)
            }
        }

        override suspend fun ByteReadPacket.decode(bot: QQAndroidBot): ImageDownPacketResponse {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        sealed class ImageDownPacketResponse : Packet {
            object Success : ImageDownPacketResponse()
        }
    }
}