(ns elephantdb.common.metadata
  (:require [elephantdb.common.domain :as dom])
  (:import [elephantdb.generated DomainSpec DomainMetaData]))

(defn mk-domain-spec
  "Returns a elephantdb.generated.DomainSpec struct
   for the supplied elephantdb.DomainSpec."
  [spec]
  (DomainSpec.
   (.getNumShards spec)
   (.getName (class (.getCoordinator spec)))
   (.getName (class (.getShardScheme spec)))))

(defn get-metadata
    "Returns a DomainMetaData struct for the supplied domain."
    [domain]
    (let [local-store (.localStore domain)
          remote-store (.remoteStore domain)
          shards (dom/shard-set domain)
          spec (.getSpec local-store)
          spec (mk-domain-spec spec)]
      (doto (DomainMetaData.)
        (.set_local_version (.mostRecentVersion local-store))
        (.set_remote_version (.mostRecentVersion remote-store))
        (.set_shard_set shards)
        (.set_domain_spec spec))))
