#  路径设置

vp: 虚拟路径，用在fs中
vurl: 虚拟统一资源定位符，用在fs中，包含机器信息

vurl: | host:sync port:engine port |

映射关系，vp -> vurl。examples:

-- fs://user/input.txt

-> fs://127.0.0.1:8070:8090/mmdpfs/user/input.txt

-> fs://127.0.0.1:8071:8091/mmdpfs/input.txt


-- fs://user/output.txt

-> fs://127.0.0.1:8070:8090/mmdpfs/user/output.txt

-> fs://127.0.0.1:8071:8091/mmdpfs/user/output.txt



# Engine

E: 对外API

TS: 生成任务说明

STS: 子任务说明

EK: 用EF分解任务，用EB执行sub tasks，合并结果后返回

EF: 分解任务, ts -> sts

EB: 处理sub tasks, 返回结果

EBS: server端



