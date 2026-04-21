package com.example.fitcontrol.feature_members.domain.use_case

import com.example.fitcontrol.feature_members.domain.model.Member
import com.example.fitcontrol.feature_members.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMembers @Inject constructor(
    private val repository: MemberRepository
) {
    operator fun invoke(): Flow<List<Member>> {
        return repository.getMembers()
    }
}