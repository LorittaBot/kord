@file:GenerateKordEnum(
    name = "InviteTargetType", valueType = INT,
    docUrl = "https://discord.com/developers/docs/resources/invite#invite-object-invite-target-types",
    entries = [
        Entry("Stream", intValue = 1),
        Entry("EmbeddedApplication", intValue = 2),
    ],
)

package dev.kord.common.entity

import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalInt
import dev.kord.common.serialization.DurationInSeconds
import dev.kord.ksp.GenerateKordEnum
import dev.kord.ksp.GenerateKordEnum.Entry
import dev.kord.ksp.GenerateKordEnum.ValueType.INT
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.DeprecationLevel.HIDDEN

public sealed interface BaseDiscordInvite {
    public val code: String
    public val guild: Optional<DiscordPartialGuild>
    public val channel: DiscordChannel?
    public val inviter: Optional<DiscordUser>
    public val targetType: Optional<InviteTargetType>
    public val targetUser: Optional<DiscordUser>
    public val targetApplication: Optional<DiscordPartialApplication>
    public val approximatePresenceCount: OptionalInt
    public val approximateMemberCount: OptionalInt
    public val expiresAt: Optional<Instant?>
    public val guildScheduledEvent: Optional<DiscordGuildScheduledEvent>
}

@Serializable
public data class DiscordInvite(
    override val code: String,
    override val guild: Optional<DiscordPartialGuild> = Optional.Missing(),
    override val channel: DiscordChannel?,
    override val inviter: Optional<DiscordUser> = Optional.Missing(),
    @SerialName("target_type")
    override val targetType: Optional<InviteTargetType> = Optional.Missing(),
    @SerialName("target_user")
    override val targetUser: Optional<DiscordUser> = Optional.Missing(),
    @SerialName("target_application")
    override val targetApplication: Optional<DiscordPartialApplication> = Optional.Missing(),
    /** @suppress */
    @Deprecated("This is no longer documented. Use 'targetType' instead.", ReplaceWith("this.targetType"), level = HIDDEN)
    @SerialName("target_user_type")
    val targetUserType: Optional<@Suppress("DEPRECATION_ERROR") TargetUserType> = Optional.Missing(),
    @SerialName("approximate_presence_count")
    override val approximatePresenceCount: OptionalInt = OptionalInt.Missing,
    @SerialName("approximate_member_count")
    override val approximateMemberCount: OptionalInt = OptionalInt.Missing,
    @SerialName("expires_at")
    override val expiresAt: Optional<Instant?> = Optional.Missing(),
    @SerialName("stage_instance")
    @Deprecated("Stages are no longer discoverable")
    val stageInstance: Optional<DiscordStageInstance> = Optional.Missing(),
    @SerialName("guild_scheduled_event")
    override val guildScheduledEvent: Optional<DiscordGuildScheduledEvent> = Optional.Missing(),
) : BaseDiscordInvite

@Serializable
public data class DiscordInviteWithMetadata(
    override val code: String,
    override val guild: Optional<DiscordPartialGuild> = Optional.Missing(),
    override val channel: DiscordChannel?,
    override val inviter: Optional<DiscordUser> = Optional.Missing(),
    @SerialName("target_type")
    override val targetType: Optional<InviteTargetType> = Optional.Missing(),
    @SerialName("target_user")
    override val targetUser: Optional<DiscordUser> = Optional.Missing(),
    @SerialName("target_application")
    override val targetApplication: Optional<DiscordPartialApplication> = Optional.Missing(),
    @SerialName("approximate_presence_count")
    override val approximatePresenceCount: OptionalInt = OptionalInt.Missing,
    @SerialName("approximate_member_count")
    override val approximateMemberCount: OptionalInt = OptionalInt.Missing,
    @SerialName("expires_at")
    override val expiresAt: Optional<Instant?> = Optional.Missing(),
    @SerialName("guild_scheduled_event")
    override val guildScheduledEvent: Optional<DiscordGuildScheduledEvent> = Optional.Missing(),
    val uses: Int,
    @SerialName("max_uses")
    val maxUses: Int,
    @SerialName("max_age")
    val maxAge: DurationInSeconds,
    val temporary: Boolean,
    @SerialName("created_at")
    val createdAt: Instant,
) : BaseDiscordInvite

@Serializable
public data class DiscordPartialInvite(
    /*
    Do not trust the docs:
    2020-11-06 This is actually never null, the endpoint(Get Guild Vanity URL) returns
    a HTTP 4xx instead when the guild has no vanity url.
     */
    val code: String?,
    val uses: Int
)
