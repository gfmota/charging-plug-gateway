import json
import sys

def generate_definitions_json(n):
    definitions = {
        "rabbit_version": "3.13.2",
        "rabbitmq_version": "3.13.2",
        "product_name": "RabbitMQ",
        "product_version": "3.13.2",
        "users": [
            {
                "name": "guest",
                "password_hash": "BMfxN8drrYcIqXZMr+pWTpDT0nMcOagMduLX0bjr4jwud/pN",
                "hashing_algorithm": "rabbit_password_hashing_sha256",
                "tags": ["administrator"],
                "limits": {}
            }
        ],
        "vhosts": [
            {
                "name": "/"
            }
        ],
        "permissions": [
            {
                "user": "guest",
                "vhost": "/",
                "configure": ".*",
                "write": ".*",
                "read": ".*"
            }
        ],
        "topic_permissions": [],
        "parameters": [],
        "global_parameters": [
            {
                "name": "internal_cluster_id",
                "value": "rabbitmq-cluster-id-YVlaHpOVFS2hZcRdaeaVgw"
            }
        ],
        "policies": [],
        "queues": [],
        "exchanges": [
            {
                "name": "charging-plug-station-topic",
                "vhost": "/",
                "type": "topic",
                "durable": True,
                "auto_delete": False,
                "internal": False,
                "arguments": {}
            }
        ],
        "bindings": []
    }

    # Generate queues and bindings for current status and daily report
    for i in range(1, n + 1):
        current_status_queue_name = f"charging-plug-station-current-status-queue-{i}"
        daily_report_queue_name = f"charging-plug-station-daily-report-queue-{i}"

        current_status_queue_entry = {
            "name": current_status_queue_name,
            "vhost": "/",
            "durable": True,
            "auto_delete": False,
            "arguments": {}
        }
        definitions["queues"].append(current_status_queue_entry)

        daily_report_queue_entry = {
            "name": daily_report_queue_name,
            "vhost": "/",
            "durable": True,
            "auto_delete": False,
            "arguments": {}
        }
        definitions["queues"].append(daily_report_queue_entry)

        current_status_binding_entry = {
            "source": "charging-plug-station-topic",
            "vhost": "/",
            "destination": current_status_queue_name,
            "destination_type": "queue",
            "routing_key": f"charging-plug-station-current-status-{i}",
            "arguments": {}
        }
        definitions["bindings"].append(current_status_binding_entry)

        daily_report_binding_entry = {
            "source": "charging-plug-station-topic",
            "vhost": "/",
            "destination": daily_report_queue_name,
            "destination_type": "queue",
            "routing_key": f"charging-plug-station-daily-report-{i}",
            "arguments": {}
        }
        definitions["bindings"].append(daily_report_binding_entry)

    return definitions

def write_definitions_json(definitions):
    with open('definitions.json', 'w') as f:
        json.dump(definitions, f, indent=2)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script.py <n>")
        sys.exit(1)

    n = int(sys.argv[1])
    definitions = generate_definitions_json(n)
    write_definitions_json(definitions)
    print(f"Generated definitions.json successfully for {n} current status and daily report queues.")
