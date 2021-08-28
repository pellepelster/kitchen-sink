#!/usr/bin/env bash


# do stuff more
set -eu

DIR="$( cd "$(dirname "$0")" ; pwd -P )"

source "${DIR}/ctuhl/lib/shell/log.sh"

COMPONENTS="todo-application"

function component_actions {
  local action=${1:-}
  shift || true

  for component in ${COMPONENTS}
  do
    component_action "${component}" "${action}"
  done
}

function component_action {
  local component=${1:-}
  shift || true
  local action=${1:-}
  shift || true

  log_divider_header "${component} -> ${action}"
  (
    cd "${DIR}/${component}"
    "./do" "${action}"
  )
  log_divider_footer
}

function task_build {
  component_actions "build"
  mkdir -p "${DIR}/build/posts"
  snex --source "${DIR}/posts" -snippets "${DIR}/todo-application" -target "${DIR}/build/posts" -template-file "${DIR}/hugo.template"
}

function task_usage {
  echo "Usage: $0 clean | build | test"
  exit 1
}

arg=${1:-}
shift || true
case ${arg} in
  clean) component_actions "clean" "$@" ;;
  generate) component_actions "generate" "$@" ;;
  build) task_build "$@" ;;
  test) component_actions "test" "$@" ;;
  *) task_usage ;;
esac
