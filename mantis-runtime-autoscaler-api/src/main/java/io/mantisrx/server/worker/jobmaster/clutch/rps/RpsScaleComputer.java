/*
 * Copyright 2021 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mantisrx.server.worker.jobmaster.clutch.rps;

import io.mantisrx.control.clutch.ClutchConfiguration;
import io.mantisrx.control.clutch.IScaleComputer;

public class RpsScaleComputer implements IScaleComputer {
    private final ClutchRpsPIDConfig rpsConfig;

    public RpsScaleComputer(ClutchRpsPIDConfig rpsConfig) {
        if (rpsConfig == null) {
            rpsConfig = ClutchRpsPIDConfig.DEFAULT;
        }
        this.rpsConfig = rpsConfig;
    }
    public Double apply(ClutchConfiguration config, Long currentScale, Double delta) {
        double scaleUpPct = rpsConfig.getScaleUpAbovePct() * 0.01;
        double scaleDownPct = rpsConfig.getScaleDownBelowPct() * 0.01;

        // unbox delta once (will throw NPE like original if delta is null)
        double d = delta;

        if (d > -scaleDownPct && d < scaleUpPct) {
            return (double) currentScale;
        }
        if (d >= scaleUpPct) {
            d *= rpsConfig.getScaleUpMultiplier();
        }
        if (d <= -scaleDownPct) {
            d *= rpsConfig.getScaleDownMultiplier();
        }

        // delta is a percentage, actual increase/decrease is computed as percentage of current scale.
        double current = currentScale.doubleValue();
        double scale = Math.round(current + current * d);

        double min = config.getMinSize();
        double max = config.getMaxSize();
        if (scale < min) {
            scale = min;
        } else if (scale > max) {
            scale = max;
        }
        return scale;
    }
}
