package com.bt.openlink.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Profile {
    @Nonnull private final List<String> parseErrors;
    @Nullable private final ProfileId profileId;
    @Nullable private final Boolean isDefault;
    @Nullable private final String device;
    @Nullable private final String label;
    @Nullable private final Boolean online;
    @Nullable private final Site site;
    @Nonnull final List<RequestAction> actions;

    private Profile(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        this.profileId = builder.profileId;
        this.isDefault = builder.isDefault;
        this.device = builder.device;
        this.label = builder.label;
        this.online = builder.online;
        this.site = builder.site;
        this.actions = builder.actions;
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = parseErrors;
        }
    }

    @Nonnull
    public List<String> getParseErrors() {
        return new ArrayList<>(parseErrors);
    }

    @Nonnull
    public Optional<ProfileId> getId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public Optional<Boolean> isDefault() {
        return Optional.ofNullable(isDefault);
    }

    @Nonnull
    public Optional<String> getDevice() {
        return Optional.ofNullable(device);
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public Optional<Boolean> isOnline() {
        return Optional.ofNullable(online);
    }

    @Nonnull
    public Optional<Site> getSite() {
        return Optional.ofNullable(site);
    }

    @Nonnull
    public List<RequestAction> getActions() {
        return actions;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        final Profile that = (Profile) o;
        return Objects.equals(this.profileId, that.profileId) &&
                Objects.equals(this.isDefault, that.isDefault) &&
                Objects.equals(this.device, that.device) &&
                Objects.equals(this.label, that.label) &&
                Objects.equals(this.online, that.online) &&
                Objects.equals(this.site, that.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, isDefault, device, label, online, site);
    }

    @Override
    public String toString() {
        return "Profile[" +
                "profileId=" + profileId +
                ", isDefault=" + isDefault +
                ", device='" + device + '\'' +
                ", label='" + label + '\'' +
                ", online=" + online +
                ", site=" + site +
                ']';
    }

    public static final class Builder {

        @Nullable private ProfileId profileId = null;
        @Nullable private Site site = null;
        @Nullable private Boolean isDefault;
        @Nullable private String device;
        @Nullable private String label;
        @Nullable private Boolean online;
        @Nonnull private final List<RequestAction> actions = new ArrayList<>();

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Profile build() {
            if (profileId == null) {
                throw new IllegalStateException("The profileId has not been set");
            }
            if (site == null) {
                throw new IllegalStateException("The site has not been set");
            }
            if (isDefault == null) {
                throw new IllegalStateException("The default indicator has not been set");
            }
            if (label == null) {
                throw new IllegalStateException("The label has not been set");
            }
            if (online == null) {
                throw new IllegalStateException("The online indicator has not been set");
            }
            return new Profile(this, null);
        }

        @Nonnull
        public Profile build(final List<String> parseErrors) {
            return new Profile(this, parseErrors);
        }

        public Builder setId(@Nonnull final ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder setDefault(final boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public Builder setDevice(@Nonnull final String device) {
            this.device = device;
            return this;
        }

        public Builder setLabel(@Nonnull final String label) {
            this.label = label;
            return this;
        }

        public Builder setOnline(final boolean online) {
            this.online = online;
            return this;
        }

        public Builder setSite(@Nonnull Site site) {
            this.site = site;
            return this;
        }

        public Builder addAction(@Nonnull RequestAction action) {
            actions.add(action);
            return this;
        }
    }
}