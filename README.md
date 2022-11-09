# Loritta's Kord Fork

A [Kord](https://github.com/kordlib/kord) fork with performance improvements and experimental features, tuned for big Discord bots, made for [Loritta](https://github.com/LorittaBot/Loritta)!

Don't expect binary and source compatibility with Kord!

## Changes and New Features
* Fix REST Module Rate Limiting Issues (https://github.com/kordlib/kord/pull/700)
* Add support for initializing a gateway connection with resume session data (https://github.com/kordlib/kord/pull/694)
* Changed various classes to `value class` to reduce unnecessary allocations and memory footprint (https://github.com/kordlib/kord/issues/711)
    * Some of the changed classes have a `isEqual` and `getHashCode` instead of `equals` and `hashCode`. Due to the nature of Kotlin value classes, you need to replace all equals and hashCode calls to `isEqual` and `getHashCode`. Custom `equals` and `hashCode` for value classes are targeted to be released in preview in Kotlin 1.8.20.
    * `Snowflake`
    * `UserFlags`
    * `DiscordBitSet`
    * `Permissions`