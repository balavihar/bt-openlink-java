package com.bt.openlink.type;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VoiceRecorderInfo implements Serializable {

    private static final long serialVersionUID = 8627794951440063523L;

    @Nullable private final RecorderNumber recorderNumber;
    @Nullable private final RecorderPort recorderPort;
    @Nullable private final RecorderChannel recorderChannel;
    @Nullable private final RecorderType recorderType;

    private VoiceRecorderInfo(@Nonnull final VoiceRecorderInfo.Builder builder) {
        this.recorderNumber = builder.recorderNumber;
        this.recorderPort = builder.recorderPort;
        this.recorderChannel = builder.recorderChannel;
        this.recorderType = builder.recorderType;
    }

    @Nonnull
    public Optional<RecorderNumber> getRecorderNumber() {
        return Optional.ofNullable(recorderNumber);
    }

    @Nonnull
    public Optional<RecorderPort> getRecorderPort() {
        return Optional.ofNullable(recorderPort);
    }

    @Nonnull
    public Optional<RecorderChannel> getRecorderChannel() {
        return Optional.ofNullable(recorderChannel);
    }

    @Nonnull
    public Optional<RecorderType> getRecorderType() {
        return Optional.ofNullable(recorderType);
    }

    public static final class Builder {

        @Nullable private RecorderNumber recorderNumber;
        @Nullable private RecorderPort recorderPort;
        @Nullable private RecorderChannel recorderChannel;
        @Nullable private RecorderType recorderType;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public VoiceRecorderInfo build() {
            if (recorderNumber == null) {
                throw new IllegalStateException("The VoiceRecorder recorderNumber has not been set");
            }
            if (recorderPort == null) {
                throw new IllegalStateException("The VoiceRecorder recorderPort has not been set");
            }
            if (recorderChannel == null) {
                throw new IllegalStateException("The VoiceRecorder recorderChannel has not been set");
            }
            if (recorderType == null) {
                throw new IllegalStateException("The VoiceRecorder recorderType has not been set");
            }
            return new VoiceRecorderInfo(this);
        }

        @Nonnull
        public VoiceRecorderInfo build(final List<String> errors) {
            if (recorderNumber == null) {
                errors.add("Invalid VoiceRecorderInfo; missing recorderNumber is mandatory");
            }
            if (recorderPort == null) {
                errors.add("Invalid VoiceRecorderInfo; missing recorderPort is mandatory");
            }
            if (recorderChannel == null) {
                errors.add("Invalid VoiceRecorderInfo; missing recorderChannel is mandatory");
            }
            if (recorderType == null) {
                errors.add("Invalid VoiceRecorderInfo; missing recorderType is mandatory");
            }
            return new VoiceRecorderInfo(this);
        }

        public Builder setRecorderNumber(@Nullable final RecorderNumber recorderNumber) {
            this.recorderNumber = recorderNumber;
            return this;
        }

        public Builder setRecorderPort(@Nullable final RecorderPort recorderPort) {
            this.recorderPort = recorderPort;
            return this;
        }

        public Builder setRecorderChannel(@Nullable final RecorderChannel recorderChannel) {
            this.recorderChannel = recorderChannel;
            return this;
        }

        public Builder setRecorderType(@Nullable final RecorderType recorderType) {
            this.recorderType = recorderType;
            return this;
        }
    }
}
